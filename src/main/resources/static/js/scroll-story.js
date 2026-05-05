/**
 * 스크롤 진행도에 따라 문구(블록)가 한 덩어리씩 나타났다 사라짐 (애플 랜딩식).
 * - 부모 [data-story-zone] 존의 진행도 0~1을 사용(글자 단위 scroll-scrub과 분리).
 * - [data-scroll-story] 안의 .story-slab 에 data-story-range="시작,끝" (0~1 구간, 해당 구간에서 페이드 인·홀드·아웃).
 * - 등장·퇴장: 아래→위 짧은 이동(translateY) + opacity — 이동은 opacity보다 ease-in으로 살짝 늦게 따라와 덜 경직되게.
 * - 선택 data-story-edge 로 인·아웃 램프 폭(기본: 구간 길이의 ~10%, 최대 0.06) → 대부분 구간은 거의 완전 표시 유지.
 * - 뷰포트 720px 이하·모션 축소: 세로로 모두 표시(스크롤 연출 비활성). 창 크기·설정 변경 시 동기화.
 */
(function () {
    function clamp(x, a, b) {
        return Math.max(a, Math.min(b, x));
    }

    function smoothstep(t) {
        t = clamp(t, 0, 1);
        return t * t * (3 - 2 * t);
    }

    function zoneProgress(zone) {
        var rect = zone.getBoundingClientRect();
        var vh = window.innerHeight || document.documentElement.clientHeight;
        var scrollY = window.scrollY || window.pageYOffset;
        var topDoc = rect.top + scrollY;
        var h = rect.height;
        var travel = Math.max(1, h - vh * 0.45);
        var raw = (scrollY - topDoc + vh * 0.32) / travel;
        return smoothstep(raw);
    }

    function parseRange(el) {
        var r = el.getAttribute("data-story-range");
        if (!r) {
            return [0, 1];
        }
        var parts = r.split(",");
        var a = parseFloat(parts[0]);
        var b = parseFloat(parts[1]);
        if (Number.isNaN(a)) {
            a = 0;
        }
        if (Number.isNaN(b)) {
            b = 1;
        }
        if (b < a) {
            var t = a;
            a = b;
            b = t;
        }
        return [a, b];
    }

    function easeInCubic(t) {
        t = clamp(t, 0, 1);
        return t * t * t;
    }

    function slabOpacity(p, a, b, edgeIn) {
        var span = b - a;
        if (span <= 0 || p <= a || p >= b) {
            return 0;
        }
        var edge;
        if (edgeIn != null && !Number.isNaN(edgeIn)) {
            edge = Math.min(edgeIn, span * 0.35);
        } else {
            /* 램프를 짧게: 같은 구간에서도 불투명(읽기) 시간이 길어짐 */
            edge = Math.min(0.045, span * 0.065);
        }
        edge = Math.max(0.01, edge);
        if (2 * edge >= span * 0.95) {
            edge = span * 0.12;
        }
        var rise = p < a + edge ? smoothstep((p - a) / edge) : 1;
        var fall = p > b - edge ? smoothstep((b - p) / edge) : 1;
        return clamp(rise * fall, 0, 1);
    }

    function paintStory(storyEl, zone) {
        var p = zoneProgress(zone);
        var slabs = storyEl.querySelectorAll(".story-slab");
        slabs.forEach(function (slab) {
            var range = parseRange(slab);
            var edgeAttr = slab.getAttribute("data-story-edge");
            var edgeParsed = edgeAttr ? parseFloat(edgeAttr) : undefined;
            var edge = edgeParsed !== undefined && !Number.isNaN(edgeParsed) ? edgeParsed : undefined;
            var v = slabOpacity(p, range[0], range[1], edge);
            var t = clamp(v, 0, 1);
            /* 이동량↓ + opacity보다 느리게 따라오는 곡선 → 스크롤에 덜 끌려 다니는 느낌 */
            var liftPx = 13;
            var tPos = easeInCubic(t);
            var y = ((1 - tPos) * liftPx).toFixed(2);
            slab.style.opacity = String(v);
            slab.style.transform = "translate3d(0, " + y + "px, 0)";
            slab.style.filter = "none";
            slab.style.pointerEvents = v > 0.12 ? "auto" : "none";
            if (v < 0.08) {
                slab.setAttribute("aria-hidden", "true");
            } else {
                slab.removeAttribute("aria-hidden");
            }
        });
    }

    function resetSlabs(story) {
        story.querySelectorAll(".story-slab").forEach(function (slab) {
            slab.style.opacity = "1";
            slab.style.transform = "none";
            slab.style.filter = "none";
            slab.style.pointerEvents = "auto";
            slab.removeAttribute("aria-hidden");
        });
    }

    var stories = document.querySelectorAll("[data-scroll-story]");
    if (!stories.length) {
        return;
    }

    var narrowMq = window.matchMedia("(max-width: 720px)");
    var reducedMq = window.matchMedia("(prefers-reduced-motion: reduce)");
    var pairs = [];
    var scheduled = false;
    var scrollBound = false;
    var onScroll;

    function rebuildPairs() {
        pairs.length = 0;
        stories.forEach(function (story) {
            var zone = story.closest("[data-story-zone]");
            if (zone) {
                pairs.push({ story: story, zone: zone });
            }
        });
    }

    function tick() {
        scheduled = false;
        if (narrowMq.matches || reducedMq.matches || !pairs.length) {
            return;
        }
        pairs.forEach(function (pair) {
            paintStory(pair.story, pair.zone);
        });
    }

    function req() {
        if (!scheduled) {
            scheduled = true;
            requestAnimationFrame(tick);
        }
    }

    function bindScroll() {
        if (scrollBound) {
            return;
        }
        onScroll = req;
        window.addEventListener("scroll", onScroll, { passive: true });
        window.addEventListener("resize", onScroll, { passive: true });
        scrollBound = true;
    }

    function unbindScroll() {
        if (!scrollBound) {
            return;
        }
        window.removeEventListener("scroll", onScroll);
        window.removeEventListener("resize", onScroll);
        scrollBound = false;
    }

    function syncMode() {
        unbindScroll();
        document.documentElement.classList.remove("use-scroll-story");

        if (narrowMq.matches || reducedMq.matches) {
            stories.forEach(resetSlabs);
            return;
        }

        rebuildPairs();
        if (!pairs.length) {
            stories.forEach(resetSlabs);
            return;
        }

        document.documentElement.classList.add("use-scroll-story");
        bindScroll();
        tick();
    }

    function onMqChange() {
        syncMode();
    }

    if (narrowMq.addEventListener) {
        narrowMq.addEventListener("change", onMqChange);
    } else {
        narrowMq.addListener(onMqChange);
    }
    if (reducedMq.addEventListener) {
        reducedMq.addEventListener("change", onMqChange);
    } else {
        reducedMq.addListener(onMqChange);
    }

    syncMode();
})();
