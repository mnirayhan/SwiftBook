// [MetaPipe Sticky Navbar Fix]
// Improved: recalculates offsets and applies styles on DOM changes and window resize for robust sticky navbar/tabbar.
(function() {
    function log(...args) {
        if (typeof console !== 'undefined') {
            console.log('[MetaPipe StickyNavbar]', ...args);
        }
    }

    // Desktop mode fix (unchanged)
    if (window.isDesktopMode && window.isDesktopMode()) {
        (() => {
            const waitForBanner = () => new Promise(resolve => {
                const existing = document.querySelector('div[role="banner"]');
                if (existing) return resolve(existing);
                new MutationObserver((mutations, obs) => {
                    for (const { addedNodes } of mutations) {
                        for (const node of addedNodes) {
                            if (node.nodeType === 1 && node.matches('div[role="banner"]')) {
                                obs.disconnect();
                                return resolve(node);
                            }
                        }
                    }
                }).observe(document.body, { childList: true, subtree: true });
            });
            const forceFixed = el => {
                if (el?.classList.contains('xixxii4')) {
                    el.style.setProperty('position', 'fixed', 'important');
                }
            };
            waitForBanner().then(banner => {
                const style = document.createElement('style');
                style.textContent = `
                  div[role="banner"].xixxii4,
                  div[role="banner"] .xixxii4 {
                    position: fixed !important;
                  }
                `;
                document.head.appendChild(style);
                forceFixed(banner);
                banner.querySelectorAll('.xixxii4').forEach(forceFixed);
                new MutationObserver(mutations => {
                    for (const m of mutations) {
                        if (m.type === 'childList') {
                            m.addedNodes.forEach(n => {
                                forceFixed(n);
                                n.querySelectorAll?.('.xixxii4')?.forEach(forceFixed);
                            });
                        } else if (m.type === 'attributes' && m.attributeName === 'class') {
                            forceFixed(m.target);
                        }
                    }
                }).observe(banner, { childList: true, subtree: true, attributes: true, attributeFilter: ['class'] });
            });
        })();
        return;
    }

    // Mobile mode fix
    function applyStyles() {
        // Selectors for navbar, tabbar, and feed scroller
        const navbar = document.querySelector('div[data-tti-phase="-1"][data-mcomponent="MContainer"][data-type="container"][data-focusable="true"].m');
        const tabbar = document.querySelector('div[role="tablist"][data-tti-phase="-1"][data-type="container"][data-mcomponent="MContainer"].m');
        const scroller = document.querySelector('div[data-type="vscroller"][data-is-pull-to-refresh-allowed="true"]');
        const hasLogo = navbar?.querySelector('div[aria-label*="Facebook"]');
        const hasFeed = tabbar?.querySelector('div[aria-label*="feed"]');
        // Remove previous inline styles to avoid stacking
        if (navbar) navbar.removeAttribute('style');
        if (tabbar) tabbar.removeAttribute('style');
        if (scroller) scroller.removeAttribute('style');
        // Calculate heights
        const navbarHeight = navbar ? (parseFloat(getComputedStyle(navbar).height) || 0) : 0;
        const tabbarHeight = tabbar ? (parseFloat(getComputedStyle(tabbar).height) || 0) : 0;
        // Apply fixed styles
        if (hasLogo && navbar) Object.assign(navbar.style, {
            position: 'fixed',
            top: '0',
            left: '0',
            width: '100%',
            zIndex: '1000',
            pointerEvents: 'auto',
            background: '#1877f2',
            transition: 'box-shadow 0.2s',
            boxShadow: '0 2px 4px rgba(0,0,0,0.08)'
        });
        if (hasFeed && tabbar) Object.assign(tabbar.style, {
            position: 'fixed',
            top: hasLogo ? navbarHeight + 'px' : '0',
            left: '0',
            width: '100%',
            zIndex: '999',
            pointerEvents: 'auto',
            background: '#fff',
            boxShadow: '0 1.5px 6px rgba(0,0,0,0.07)'
        });
        // Offset feed content
        if (scroller) {
            const offset = (hasLogo ? navbarHeight : 0) + (hasFeed ? tabbarHeight : 0);
            const scrollContent = scroller.querySelector(':scope > div:not(.pull-to-refresh-spinner-container)');
            if (scrollContent) {
                scrollContent.style.marginTop = offset + 'px';
            } else {
                scroller.style.paddingTop = offset + 'px';
            }
            if (window.isFeed && window.isFeed()) scroller.style.paddingBottom = '0';
            // Spinner z-index
            const spinnerContainer = scroller.querySelector('.pull-to-refresh-spinner-container');
            if (spinnerContainer) spinnerContainer.style.zIndex = '1001';
            const spinner = scroller.querySelector('.pull-to-refresh-spinner');
            if (spinner) spinner.style.margin = '0 auto';
        }
        // Body reset
        Object.assign(document.body.style, {
            paddingTop: '0',
            marginTop: '0',
            overflow: 'visible',
            height: '100%'
        });
    }

    // Use requestAnimationFrame for layout safety
    function rafApply() { requestAnimationFrame(applyStyles); }
    rafApply();
    // Observe DOM changes
    const mo = new MutationObserver(rafApply);
    mo.observe(document.body, { childList: true, subtree: true });
    // Reapply on window resize
    window.addEventListener('resize', rafApply);
    log('Sticky navbar script loaded');
})();
