(function() {

    if (isDesktopMode()) {
        (function() {
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

          // Desktop selectors for sponsored content
          const desktopSelectors = [
              // Main feed posts with sponsored text
              'div[data-pagelet*="FeedUnit"]',
              'div[data-testid="post_container"]',
              'div[role="article"]',
              // Stories and reels that might be sponsored
              'div[data-testid="story_container"]',
              'div[data-testid="reel_container"]',
              // Right sidebar sponsored content
              'div[data-testid="right_column"] div[role="complementary"]',
              // Marketplace and other sponsored sections
              'div[data-testid="marketplace"]',
              'div[data-testid="sponsored_content"]'
          ];

          function hideDesktopSponsoredContent() {
              desktopSelectors.forEach(selector => {
                  const elements = document.querySelectorAll(selector);
                  elements.forEach(element => {
                      // Check if element contains sponsored text
                      const textContent = element.textContent || '';
                      if (sponsoredRegex.test(textContent)) {
                          // Try to find the closest parent that looks like a post container
                          let container = element;
                          for (let i = 0; i < 5; i++) {
                              if (container.parentElement) {
                                  container = container.parentElement;
                                  // Check if this looks like a post container
                                  if (container.getAttribute('data-testid') === 'post_container' ||
                                      container.getAttribute('role') === 'article' ||
                                      container.querySelector('[data-testid="post_container"]') ||
                                      container.querySelector('[role="article"]')) {
                                      break;
                                  }
                              }
                          }
                          container.style.display = 'none';
                      }
                  });
              });
          }

          // Also try the old selectors as fallback
          const oldSelectors = 'div.sponsored_ad, article[data-ft*="sponsored_ad"], div[data-ft*="sponsored_ad"]';
          const oldElements = document.querySelectorAll(oldSelectors);
          oldElements.forEach(el => el.remove());

          hideDesktopSponsoredContent();

          const observer = new MutationObserver((mutations) => {
              let shouldCheck = false;
              for (const mutation of mutations) {
                  if (mutation.type === 'childList' && mutation.addedNodes.length > 0) {
                      shouldCheck = true;
                      break;
                  }
              }
              if (shouldCheck) {
                  hideDesktopSponsoredContent();
              }
          });

          observer.observe(document.body, {
              childList: true,
              subtree: true
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