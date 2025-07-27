(function() {
    // Utility: log with prefix
    function log(...args) {
        if (typeof console !== 'undefined') {
            console.log('[MetaPipe FB Lite UI]', ...args);
        }
    }

    log('FB Lite UI script started');

    // Only apply to mobile version
    if (isDesktopMode()) {
        log('Desktop mode detected, skipping FB Lite UI');
        return;
    }

    function applyFBLiteStyles() {
        // Create and inject CSS for Facebook Official App-like appearance
        const style = document.createElement('style');
        style.textContent = `
            /* Facebook Official App-like UI Styles */
            body {
                background: #f0f2f5 !important;
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Arial, sans-serif !important;
            }

            /* Header/Navigation */
            div[role="banner"] {
                background: #1877f2 !important;
                color: #fff !important;
                box-shadow: 0 2px 4px rgba(0,0,0,0.08) !important;
                border-bottom: none !important;
                min-height: 56px !important;
                display: flex !important;
                align-items: center !important;
                padding: 0 16px !important;
            }

            /* Feed posts as cards */
            div[data-type="vscroller"] > div {
                background: #fff !important;
                border-radius: 12px !important;
                margin: 16px 8px !important;
                box-shadow: 0 1.5px 6px rgba(0,0,0,0.07) !important;
                border: none !important;
                padding: 16px 0 12px 0 !important;
            }
            div[data-type="vscroller"] > div + div {
                margin-top: 18px !important;
            }
            div[data-type="vscroller"] > div {
                border-bottom: 1px solid #e4e6eb !important;
            }
            div[data-type="vscroller"] > div:last-child {
                border-bottom: none !important;
            }

            /* Post content */
            div[data-mcomponent="MContainer"] {
                padding: 0 20px 12px 20px !important;
            }

            /* Profile avatars */
            img[alt][src*="scontent"], img[alt][src*="profile"] {
                border-radius: 50% !important;
                width: 52px !important;
                height: 52px !important;
                object-fit: cover !important;
                border: 3px solid #e4e6eb !important;
                margin-right: 12px !important;
            }

            /* User names */
            span[dir="auto"] {
                font-weight: 700 !important;
                color: #050505 !important;
                font-size: 16px !important;
            }

            /* Timestamps */
            span[dir="auto"][style*="color: rgb(108, 117, 125)"] {
                color: #65676b !important;
                font-size: 13px !important;
                font-weight: 400 !important;
            }

            /* Post text */
            .native-text, div[data-mcomponent="MContainer"] span[dir="auto"] {
                font-size: 16px !important;
                color: #050505 !important;
                line-height: 1.6 !important;
            }

            /* Like/Comment/Share buttons */
            div[role="button"][aria-label*="Like"],
            div[role="button"][aria-label*="Comment"],
            div[role="button"][aria-label*="Share"] {
                background: #f0f2f5 !important;
                border-radius: 20px !important;
                padding: 12px 14px !important;
                margin: 6px 4px !important;
                font-weight: 600 !important;
                color: #65676b !important;
                font-size: 15px !important;
                display: inline-block !important;
                border: none !important;
                transition: all 0.18s cubic-bezier(.4,0,.2,1) !important;
                min-width: 44px !important;
                min-height: 44px !important;
                border-radius: 22px !important;
                margin: 8px 6px !important;
                font-size: 16px !important;
                box-shadow: 0 1px 2px rgba(24,119,242,0.04);
            }
            div[role="button"][aria-label*="Like"].active,
            div[role="button"][aria-label*="Like"]:active {
                background: #e7f3ff !important;
                color: #1877f2 !important;
            }
            div[role="button"][aria-label*="Comment"].active,
            div[role="button"][aria-label*="Comment"]:active,
            div[role="button"][aria-label*="Share"].active,
            div[role="button"][aria-label*="Share"]:active {
                background: #f0f2f5 !important;
                color: #1877f2 !important;
            }
            div[role="button"][aria-label*="Like"]:hover,
            div[role="button"][aria-label*="Comment"]:hover,
            div[role="button"][aria-label*="Share"]:hover {
                background-color: #e4e6eb !important;
                color: #1877f2 !important;
            }
            div[role="button"]:active {
                transform: scale(0.97);
                box-shadow: 0 1px 4px rgba(24,119,242,0.08);
            }

            /* Story bar and highlights */
            div[data-pagelet*="Stories"] {
                display: flex !important;
                flex-direction: row !important;
                overflow-x: auto !important;
                background: #fff !important;
                border-radius: 12px !important;
                margin: 12px 8px 0 8px !important;
                box-shadow: 0 1.5px 6px rgba(0,0,0,0.07) !important;
                padding: 12px 0 !important;
            }
            div[data-pagelet*="Stories"] > div {
                margin-right: 12px !important;
            }
            div[data-pagelet*="Stories"] > div > div {
                border-radius: 50% !important;
                border: 3px solid #1877f2 !important;
                width: 60px !important;
                height: 60px !important;
            }

            /* Bottom navigation */
            div[role="navigation"] {
                background: #fff !important;
                border-top: 1px solid #e4e6eb !important;
                box-shadow: 0 -2px 8px rgba(0,0,0,0.08) !important;
                min-height: 56px !important;
                display: flex !important;
                align-items: center !important;
                justify-content: space-around !important;
                border-radius: 16px 16px 0 0 !important;
            }
            div[role="navigation"] > div > div {
                border-radius: 12px !important;
                margin: 6px !important;
                padding: 12px 14px !important;
                font-size: 20px !important;
                transition: all 0.18s cubic-bezier(.4,0,.2,1) !important;
            }
            div[role="navigation"] > div > div:hover {
                background-color: #f0f2f5 !important;
            }

            /* Search bar */
            div[role="search"] {
                background: #f0f2f5 !important;
                border-radius: 20px !important;
                border: 1px solid #e4e6eb !important;
                box-shadow: 0 1px 2px rgba(0,0,0,0.07) !important;
                padding: 8px 16px !important;
                margin: 12px 8px !important;
            }

            /* Right sidebar (if visible) */
            div[data-testid="right_column"] {
                background: #fff !important;
                border-radius: 12px !important;
                margin: 8px !important;
                box-shadow: 0 1.5px 6px rgba(0,0,0,0.07) !important;
            }

            /* Compact spacing */
            div[data-mcomponent="MContainer"] > div {
                margin: 6px 0 !important;
            }

            /* Remove unnecessary borders */
            div[style*="border"] {
                border: none !important;
            }

            /* Section headers */
            h1, h2, h3, h4, h5, h6 {
                font-weight: 700 !important;
                color: #050505 !important;
                font-size: 20px !important;
                margin: 0 0 8px 0 !important;
            }

            /* Ensure images and videos fit the screen */
            div[data-type="vscroller"] img,
            div[data-type="vscroller"] video,
            div[data-mcomponent="MContainer"] img,
            div[data-mcomponent="MContainer"] video,
            div[data-pagelet*="Stories"] img,
            div[data-pagelet*="Stories"] video {
                max-width: 100% !important;
                height: auto !important;
                display: block !important;
                margin-left: auto !important;
                margin-right: auto !important;
            }

            /* Ensure SVGs and icons use their original colors */
            svg, img[role="img"], i, ._6xu4, ._3-8_ {
                color: inherit !important;
                fill: inherit !important;
                background: none !important;
                filter: none !important;
            }

            /* Hide unwanted elements (optional) */
            div:has([aria-label*="Suggested for you"]),
            div:has([aria-label*="Sponsored"]) {
                display: none !important;
            }
            div:has([aria-label*="People You May Know"]),
            div:has([aria-label*="Groups"]),
            div:has([aria-label*="Events"]),
            div:has([aria-label*="Games"]),
            div:has([aria-label*="Watch"]),
            div:has([aria-label*="Marketplace"]),
            div:has([aria-label*="Reels"]),
            div:has([aria-label*="Live"]) {
                display: none !important;
            }
            div[data-pagelet*="Stories"],
            div[data-pagelet*="Rooms"] {
                display: none !important;
            }
        `;

        // Remove existing FB Lite styles if any
        const existingStyle = document.getElementById('fb-lite-ui-style');
        if (existingStyle) {
            existingStyle.remove();
        }

        style.id = 'fb-lite-ui-style';
        document.head.appendChild(style);
        log('FB Lite UI styles applied');
    }

    // Apply styles immediately
    applyFBLiteStyles();

    // Watch for dynamic content changes
    const observer = new MutationObserver((mutations) => {
        let shouldReapply = false;
        for (const mutation of mutations) {
            if (mutation.type === 'childList' && mutation.addedNodes.length > 0) {
                // Check if new posts or UI elements were added
                for (const node of mutation.addedNodes) {
                    if (node.nodeType === 1 && (
                        node.querySelector('[data-type="vscroller"]') ||
                        node.querySelector('[data-pagelet*="Stories"]') ||
                        node.querySelector('[role="banner"]') ||
                        node.querySelector('[role="navigation"]')
                    )) {
                        shouldReapply = true;
                        break;
                    }
                }
            }
        }
        if (shouldReapply) {
            log('New content detected, reapplying FB Lite UI styles');
            setTimeout(applyFBLiteStyles, 100);
        }
    });

    observer.observe(document.body, {
        childList: true,
        subtree: true
    });

    log('FB Lite UI script completed');
})();

// Add this to your JS after the CSS block
if (!document.getElementById('fb-lite-scroll-top')) {
    const btn = document.createElement('button');
    btn.id = 'fb-lite-scroll-top';
    btn.textContent = '↑';
    btn.style.position = 'fixed';
    btn.style.bottom = '80px';
    btn.style.right = '20px';
    btn.style.zIndex = '9999';
    btn.style.background = '#1877f2';
    btn.style.color = '#fff';
    btn.style.border = 'none';
    btn.style.borderRadius = '50%';
    btn.style.width = '48px';
    btn.style.height = '48px';
    btn.style.fontSize = '24px';
    btn.style.boxShadow = '0 2px 8px rgba(0,0,0,0.15)';
    btn.style.display = 'none';
    btn.onclick = () => window.scrollTo({top: 0, behavior: 'smooth'});
    document.body.appendChild(btn);
    window.addEventListener('scroll', () => {
        btn.style.display = window.scrollY > 300 ? 'block' : 'none';
    });
}

// Add this to your JS after the CSS block
if (!document.getElementById('fb-lite-loading-bar')) {
    const bar = document.createElement('div');
    bar.id = 'fb-lite-loading-bar';
    bar.style.position = 'fixed';
    bar.style.top = '0';
    bar.style.left = '0';
    bar.style.width = '0%';
    bar.style.height = '3px';
    bar.style.background = '#1877f2';
    bar.style.zIndex = '99999';
    bar.style.transition = 'width 0.3s';
    document.body.appendChild(bar);
    document.addEventListener('readystatechange', () => {
        if (document.readyState === 'interactive') bar.style.width = '60%';
        if (document.readyState === 'complete') bar.style.width = '100%';
        setTimeout(() => { bar.style.width = '0%'; }, 800);
    });
}