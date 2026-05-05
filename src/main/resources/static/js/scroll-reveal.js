/**
 * 스크롤 시 섹션 안의 .sr-el 이 순차로 드러남.
 * - html 에 use-scroll-reveal 클래스를 붙인 뒤에만 CSS로 숨김(스크립트 실패 시에도 글이 보임).
 * - 이미 화면에 걸쳐 있는 섹션은 IntersectionObserver 대기 없이 즉시 공개.
 */
(function () {
    function init() {
        var sections = document.querySelectorAll(".scroll-reveal");
        if (!sections.length) {
            return;
        }

        document.documentElement.classList.add("use-scroll-reveal");

        if (window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
            sections.forEach(function (sec) {
                sec.classList.add("is-visible");
            });
            return;
        }

        if (typeof IntersectionObserver === "undefined") {
            sections.forEach(function (sec) {
                sec.classList.add("is-visible");
            });
            return;
        }

        function staggerDelays(section) {
            var items = section.querySelectorAll(".sr-el");
            for (var i = 0; i < items.length; i++) {
                items[i].style.setProperty("--sr-delay", i * 55 + "ms");
            }
        }

        function revealSection(sec, obs) {
            if (sec.classList.contains("is-visible")) {
                return;
            }
            staggerDelays(sec);
            sec.classList.add("is-visible");
            if (obs) {
                obs.unobserve(sec);
            }
        }

        function flushVisible(obs) {
            var vh = window.innerHeight || document.documentElement.clientHeight;
            sections.forEach(function (sec) {
                if (sec.classList.contains("is-visible")) {
                    return;
                }
                var r = sec.getBoundingClientRect();
                if (r.top < vh && r.bottom > 0) {
                    revealSection(sec, obs);
                }
            });
        }

        var observer = new IntersectionObserver(
            function (entries) {
                entries.forEach(function (entry) {
                    if (!entry.isIntersecting) {
                        return;
                    }
                    revealSection(entry.target, observer);
                });
            },
            { root: null, rootMargin: "0px 0px 4% 0px", threshold: 0.06 }
        );

        sections.forEach(function (sec) {
            observer.observe(sec);
        });

        flushVisible(observer);
        requestAnimationFrame(function () {
            flushVisible(observer);
            requestAnimationFrame(function () {
                flushVisible(observer);
            });
        });

        window.addEventListener(
            "load",
            function () {
                flushVisible(observer);
            },
            { once: true }
        );
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", init);
    } else {
        init();
    }
})();
