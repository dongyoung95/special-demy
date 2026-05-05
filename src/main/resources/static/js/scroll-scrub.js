/**
 * 애플 제품 페이지 뉘앙스: 스크롤 진행에 따라 글자(글리프)가 순서대로 드러남.
 * - [data-scroll-scrub] 안의 [data-scrub-chars] 텍스트를 글자 단위 span.scrub-ch 로 쪼갬.
 * - [data-scrub-block] 은 한 덩어리로 나중에 페이드인 (본문에 strong 등 마크업 유지용).
 * - 뷰포트 max-width 720px(모바일·좁은 창)에서는 비활성화: DOM은 그대로 두고 터치 스크롤 UX를 우선.
 * - [data-scroll-story]가 있는 존은 scroll-story.js가 문구 단위로 처리하므로 여기서는 제외.
 * - 홈 히어로 등은 [data-story-zone]만 쓰고 이 스크립트를 로드하지 않는 것을 권장(캐시된 구버전 오동작 방지).
 */
(function () {
    function clamp(x, a, b) {
        return Math.max(a, Math.min(b, x));
    }

    function smoothstep(t) {
        t = clamp(t, 0, 1);
        return t * t * (3 - 2 * t);
    }

    function wrapCharSpans(el) {
        if (!el || el.dataset.scrubCharsWrapped) {
            return;
        }
        el.dataset.scrubCharsWrapped = "1";
        var text = el.textContent;
        el.textContent = "";
        var chars = Array.from(text);
        for (var k = 0; k < chars.length; k++) {
            var s = document.createElement("span");
            s.className = "scrub-ch";
            s.textContent = chars[k];
            el.appendChild(s);
        }
    }

    function buildSequence(zone) {
        zone.querySelectorAll("[data-scrub-chars]").forEach(wrapCharSpans);
        var seq = [];
        zone.querySelectorAll(".scrub-ch").forEach(function (ch) {
            seq.push(ch);
        });
        zone._scrubSeq = seq;
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

    function paintZone(zone) {
        var p = zoneProgress(zone);
        var seq = zone._scrubSeq || [];
        var n = seq.length;
        if (n === 0) {
            return;
        }
        var slot = 0.62 / Math.max(10, n * 1.05);
        for (var i = 0; i < n; i++) {
            var start = (i / Math.max(1, n - 1)) * 0.94;
            var t0 = (p - start) / slot;
            t0 = clamp(t0, 0, 1);
            var t = smoothstep(t0);
            /* 등장 시 살짝 늦게 올라오도록(이징), 블러·이동량 완화 */
            var tMove = 1 - Math.pow(1 - t, 2.4);
            var node = seq[i];
            node.style.opacity = String(t);
            node.style.transform =
                "translateY(" + (1 - tMove) * 9 + "px) scale(" + (0.985 + tMove * 0.015) + ")";
            node.style.filter = "blur(" + (1 - tMove) * 1.25 + "px)";
        }
        zone.querySelectorAll("[data-scrub-block]").forEach(function (el) {
            var st = Number(el.getAttribute("data-scrub-start")) || 0.55;
            var dur = Number(el.getAttribute("data-scrub-dur")) || 0.12;
            var tRaw = clamp((p - st) / dur, 0, 1);
            var t = smoothstep(tRaw);
            el.style.opacity = String(t);
            var tBlockMove = 1 - Math.pow(1 - t, 2.2);
            el.style.transform = "translateY(" + (1 - tBlockMove) * 11 + "px)";
        });
    }

    var zones = Array.prototype.slice.call(document.querySelectorAll("[data-scroll-scrub]")).filter(function (z) {
        return !z.querySelector("[data-scroll-story]");
    });
    if (!zones.length) {
        return;
    }

    var narrow = window.matchMedia("(max-width: 720px)");
    if (narrow.matches) {
        return;
    }

    document.documentElement.classList.add("use-scroll-scrub");

    if (window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
        zones.forEach(function (z) {
            buildSequence(z);
            z.querySelectorAll(".scrub-ch, [data-scrub-block]").forEach(function (n) {
                n.style.opacity = "1";
                n.style.transform = "none";
                n.style.filter = "none";
            });
        });
        return;
    }

    zones.forEach(buildSequence);

    var scheduled = false;
    function tick() {
        scheduled = false;
        zones.forEach(paintZone);
    }
    function req() {
        if (!scheduled) {
            scheduled = true;
            requestAnimationFrame(tick);
        }
    }
    window.addEventListener("scroll", req, { passive: true });
    window.addEventListener("resize", req, { passive: true });
    tick();
})();
