(function() {
    // Utility: log with prefix
    function log(...args) {
        if (typeof console !== 'undefined') {
            console.log('[MetaPipe Adblock]', ...args);
        }
    }

    if (isDesktopMode()) {
        (function() {
            log('Desktop adblock script started');
            // Desktop mode sponsored content detection
            const sponsoredTexts = [
                "Sponsored", "Gesponsert", "Sponsorlu", "Sponsorowane",
                "Ispoonsara godhameera", "Geborg", "Bersponsor", "Ditaja",
                "Disponsori", "Giisponsoran", "Sponzorováno", "Sponsoreret",
                "Publicidad", "May Sponsor", "Sponsorisée", "Sponsorisé", "Oipytyvôva",
                "Ɗaukar Nayin", "Sponzorirano", "Uterwa inkunga", "Sponsorizzato",
                "Imedhaminiwa", "Hirdetés", "Misy Mpiantoka", "Gesponsord",
                "Sponset", "Patrocinado", "Sponsorizat", "Sponzorované",
                "Sponsoroitu", "Sponsrat", "Được tài trợ", "Χορηγούμενη",
                "Спонсорирано", "Спонзорирано", "Ивээн тэтгэсэн", "Реклама",
                "Спонзорисано", "במימון", "سپانسرڈ", "دارای پشتیبانی مالی",
                "ስፖንሰር የተደረገ", "प्रायोजित", "ተደረገ", "प", "স্পনসর্ড",
                "ਪ੍ਰਯੋਜਿਤ", "પ્રાયોજિત", "ପ୍ରାୟୋଜିତ", "செய்யப்பட்ட செய்யப்பட்ட",
                "చేయబడినది చేయబడినది", "ಪ್ರಾಯೋಜಿಸಲಾಗಿದೆ", "ചെയ്‌തത് ചെയ്‌തത്",
                "ලද ලද ලද", "สนับสนุน สนับสนุน รับ สนับสนุน สนับสนุน",
                "ကြော်ငြာ ကြော်ငြာ", "ឧបត្ថម្ភ ឧបត្ថម្ភ ឧបត្ថម្ភ", "광고",
                "贊助", "赞助内容", "広告", "സ്‌പോൺസർ ചെയ്‌തത്"
            ];
            const sponsoredRegex = new RegExp(sponsoredTexts.join('|'), 'i');

            // Broader selectors for desktop posts
            const desktopSelectors = [
                'div[data-pagelet*="FeedUnit"]',
                'div[data-testid="post_container"]',
                'div[role="article"]',
                'div[data-pagelet*="Stories"]',
                'div[data-pagelet*="RightRail"]',
                'div[data-pagelet*="VideoChatHomeUnit"]',
                'div[data-pagelet*="GroupsFeed"]',
                'div[data-pagelet*="WatchFeed"]',
                'div[data-pagelet*="FeedUnit_*"]',
                'div[data-pagelet*="FeedUnitGroup"]',
                'div[data-pagelet*="FeedUnitCluster"]',
                'div[data-pagelet*="Feed"]',
                'div[data-pagelet*="Stories"]',
                'div[data-pagelet*="Reels"]',
                'div[data-testid="story_container"]',
                'div[data-testid="reel_container"]',
                'div[data-testid="right_column"] div[role="complementary"]',
                'div[data-testid="marketplace"]',
                'div[data-testid="sponsored_content"]',
                'div[role="complementary"]',
                'section[role="region"]',
                'div[aria-label*="Sponsored"]',
                'div[aria-label*="Ad"]',
            ];

            function hideDesktopSponsoredContent() {
                let hiddenCount = 0;
                desktopSelectors.forEach(selector => {
                    const elements = document.querySelectorAll(selector);
                    elements.forEach(element => {
                        // Check for sponsored text in element or its descendants
                        let found = false;
                        // Check direct text
                        if (sponsoredRegex.test(element.textContent || '')) {
                            found = true;
                } else {
                            // Check for specific spans/divs with sponsored text
                            const spans = element.querySelectorAll('span, div, a');
                            for (const span of spans) {
                                if (sponsoredRegex.test(span.textContent || '')) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (found) {
                            // Try to find the closest parent that looks like a post container
                            let container = element;
                            for (let i = 0; i < 5; i++) {
                                if (container.parentElement) {
                                    container = container.parentElement;
                                    if (
                                        container.getAttribute('data-testid') === 'post_container' ||
                                        container.getAttribute('role') === 'article' ||
                                        container.querySelector('[data-testid="post_container"]') ||
                                        container.querySelector('[role="article"]')
                                    ) {
                                        break;
                                    }
                                }
                            }
                            if (container && container.style) {
                                container.style.display = 'none';
                                hiddenCount++;
                                log('Hid sponsored post:', container);
                            }
                        }
                    });
                });
                if (hiddenCount > 0) log('Total sponsored posts hidden this run:', hiddenCount);
            }

            // Also try the old selectors as fallback
            const oldSelectors = 'div.sponsored_ad, article[data-ft*="sponsored_ad"], div[data-ft*="sponsored_ad"]';
            const oldElements = document.querySelectorAll(oldSelectors);
            oldElements.forEach(el => { el.remove(); log('Removed old sponsored element:', el); });

            hideDesktopSponsoredContent();

            // Improved observer: watch for subtree changes, not just childList
            const observer = new MutationObserver((mutations) => {
                let shouldCheck = false;
                for (const mutation of mutations) {
                    if (
                        mutation.type === 'childList' && mutation.addedNodes.length > 0 ||
                        mutation.type === 'subtree' ||
                        mutation.type === 'attributes'
                    ) {
                        shouldCheck = true;
                        break;
                    }
                }
                if (shouldCheck) {
                    log('MutationObserver triggered, running adblock...');
                    hideDesktopSponsoredContent();
                }
            });

          observer.observe(document.body, {
            childList: true,
                subtree: true,
                attributes: false
          });
        })();
        return;
    }


    const sponsoredTexts = [
        "Sponsored", "Gesponsert", "Sponsorlu", "Sponsorowane",
        "Ispoonsara godhameera", "Geborg", "Bersponsor", "Ditaja",
        "Disponsori", "Giisponsoran", "Sponzorováno", "Sponsoreret",
        "Publicidad", "May Sponsor", "Sponsorisée", "Sponsorisé", "Oipytyvôva",
        "Ɗaukar Nayin", "Sponzorirano", "Uterwa inkunga", "Sponsorizzato",
        "Imedhaminiwa", "Hirdetés", "Misy Mpiantoka", "Gesponsord",
        "Sponset", "Patrocinado", "Sponsorizat", "Sponzorované",
        "Sponsoroitu", "Sponsrat", "Được tài trợ", "Χορηγούμενη",
        "Спонсорирано", "Спонзорирано", "Ивээн тэтгэсэн", "Реклама",
        "Спонзорисано", "במימון", "سپانسرڈ", "دارای پشتیبانی مالی",
        "ስፖንሰር የተደረገ", "प्रायोजित", "ተደረገ", "प", "স্পনসর্ড",
        "ਪ੍ਰਯੋਜਿਤ", "પ્રાયોજિત", "ପ୍ରାୟୋଜିତ", "செய்யப்பட்ட செய்யப்பட்ட",
        "చేయబడినది చేయబడినది", "ಪ್ರಾಯೋಜಿಸಲಾಗಿದೆ", "ചെയ്‌തത് ചെയ്‌തത്",
        "ලද ලද ලද", "สนับสนุน สนับสนุน รับ สนับสนุน สนับสนุน",
        "ကြော်ငြာ ကြော်ငြာ", "ឧបត្ថម្ភ ឧបត្ថម្ភ ឧបត្ថម្ភ", "광고",
        "贊助", "赞助内容", "広告", "സ്‌പോൺസർ ചെയ്‌തത്"
    ];

    const sponsoredRegex = new RegExp(sponsoredTexts.join('|'), 'i');

    function hideSponsoredContent(config) {
        const { selector, textSelector } = config;
        const containers = document.querySelectorAll(selector);

        containers.forEach(container => {
            const spans = container.querySelectorAll(textSelector);
            for (const span of spans) {
                if (sponsoredRegex.test(span.textContent)) {
                    container.style.display = 'none';
                    break;
                }
            }
        });
    }

    const configs = [
        {
            selector: 'div[data-type="vscroller"] div[data-tracking-duration-id]:has(> div[data-focusable="true"] div[data-mcomponent*="TextArea"] .native-text > span)',
            textSelector: '.native-text > span'
        },
        {
            selector: 'div[data-status-bar-color] > div[data-mcomponent="MContainer"] > div[data-mcomponent="MContainer"]',
            textSelector: 'div[data-mcomponent="TextArea"] .native-text > span'
        },
        {
            selector: 'div[data-mcomponent="MContainer"].m.bg-s3 div[data-mcomponent="MContainer"]',
            textSelector: 'div[data-mcomponent="TextArea"] .native-text > span'
        },

    ];

    function hideAllAds() { configs.forEach(hideSponsoredContent); }

    hideAllAds();

    const observer = new MutationObserver(hideAllAds);
    observer.observe(document.body, { childList: true, subtree: true });
})();