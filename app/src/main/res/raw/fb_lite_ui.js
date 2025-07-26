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
        // Create and inject CSS for Facebook Lite-like appearance
        const style = document.createElement('style');
        style.textContent = `
            /* FB Lite UI Styles */
            body {
                background-color: #f0f2f5 !important;
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif !important;
            }

            /* Header/Navigation */
            div[role="banner"] {
                background: linear-gradient(135deg, #1877f2 0%, #42a5f5 100%) !important;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1) !important;
                border-bottom: none !important;
            }

            /* Feed posts */
            div[data-type="vscroller"] > div {
                background: white !important;
                border-radius: 12px !important;
                margin: 8px 12px !important;
                box-shadow: 0 1px 3px rgba(0,0,0,0.1) !important;
                border: none !important;
            }

            /* Post content */
            div[data-mcomponent="MContainer"] {
                padding: 12px !important;
            }

            /* Story bubbles */
            div[data-pagelet*="Stories"] {
                background: white !important;
                border-radius: 12px !important;
                margin: 8px 12px !important;
                box-shadow: 0 1px 3px rgba(0,0,0,0.1) !important;
            }

            /* Story items */
            div[data-pagelet*="Stories"] > div > div {
                border-radius: 50% !important;
                border: 3px solid #1877f2 !important;
            }

            /* Like/Comment buttons */
            div[role="button"][aria-label*="Like"],
            div[role="button"][aria-label*="Comment"],
            div[role="button"][aria-label*="Share"] {
                background: transparent !important;
                border-radius: 20px !important;
                padding: 8px 12px !important;
                margin: 4px !important;
                transition: background-color 0.2s !important;
            }

            div[role="button"][aria-label*="Like"]:hover,
            div[role="button"][aria-label*="Comment"]:hover,
            div[role="button"][aria-label*="Share"]:hover {
                background-color: #f0f2f5 !important;
            }

            /* Text content */
            .native-text {
                font-size: 14px !important;
                line-height: 1.4 !important;
                color: #1c1e21 !important;
            }

            /* User names */
            span[dir="auto"] {
                font-weight: 600 !important;
                color: #1877f2 !important;
            }

            /* Timestamps */
            span[dir="auto"][style*="color: rgb(108, 117, 125)"] {
                color: #65676b !important;
                font-size: 12px !important;
            }

            /* Bottom navigation */
            div[role="navigation"] {
                background: white !important;
                border-top: 1px solid #e4e6ea !important;
                box-shadow: 0 -2px 4px rgba(0,0,0,0.1) !important;
            }

            /* Navigation items */
            div[role="navigation"] > div > div {
                border-radius: 8px !important;
                margin: 4px !important;
                transition: background-color 0.2s !important;
            }

            div[role="navigation"] > div > div:hover {
                background-color: #f0f2f5 !important;
            }

            /* Search bar */
            div[role="search"] {
                background: white !important;
                border-radius: 20px !important;
                border: 1px solid #e4e6ea !important;
                box-shadow: 0 1px 2px rgba(0,0,0,0.1) !important;
            }

            /* Right sidebar (if visible) */
            div[data-testid="right_column"] {
                background: white !important;
                border-radius: 12px !important;
                margin: 8px !important;
                box-shadow: 0 1px 3px rgba(0,0,0,0.1) !important;
            }

            /* Compact spacing */
            div[data-mcomponent="MContainer"] > div {
                margin: 4px 0 !important;
            }

            /* Remove unnecessary borders */
            div[style*="border"] {
                border: none !important;
            }

            /* Smooth transitions */
            * {
                transition: all 0.2s ease !important;
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