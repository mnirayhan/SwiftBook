(function() {
  // Utility: log with prefix
  function log(...args) {
    if (typeof console !== 'undefined') {
      console.log('[SwiftBook AMOLED]', ...args);
    }
  }

  log('AMOLED black script started');

  const colorRegex = /background-color\s*:\s*(#242526|#18191a|#3a3b3c|#1c1e21|rgba\s*\(\s*36\s*,\s*37\s*,\s*38\s*,\s*1\.?0*\s*\)|rgba\s*\(\s*(\d{1,3})\s*,\s*(\d{1,3})\s*,\s*(\d{1,3})\s*,\s*1\.?0*\s*\)|#([0-9a-fA-F]{6}))\s*;/gi;

  function isLightGray(r, g, b) {
    return r >= 50 && r <= 150 && g >= 50 && g <= 150 && b >= 50 && b <= 150 &&
           Math.abs(r - g) <= 20 && Math.abs(g - b) <= 20 && Math.abs(r - b) <= 20;
  }

  function processStyles() {
    const meta = document.querySelector('meta[name="theme-color"]');
    if (meta && meta.getAttribute('content') && meta.getAttribute('content').toLowerCase().trim() !== '#242526') {
      log('Meta theme-color before:', meta.getAttribute('content'));
    }
    if (meta?.getAttribute('content')?.toLowerCase().trim() !== '#242526') {
      meta?.setAttribute('content', '#000000');
      log('Meta theme-color set to #000000');
    }

    // Style tags
    document.querySelectorAll('style').forEach(style => {
      if (style.sheet?.cssRules) {
        try {
          Array.from(style.sheet.cssRules).forEach(rule => {
            const bg = rule.style?.backgroundColor?.replace(/\s+/g, '').toLowerCase();
            if (bg === '#242526' || bg === '#18191a' || bg === '#3a3b3c' || bg === '#1c1e21' || bg === 'rgba(36,37,38,1)' || bg === 'rgba(36,37,38,1.0)') {
              rule.style.backgroundColor = '#000000';
              log('Changed rule background to #000000:', rule.selectorText);
            } else if (bg && bg.startsWith('rgba(')) {
              const match = bg.match(/rgba\((\d+),(\d+),(\d+),1\.?0*\)/);
              if (match && isLightGray(...match.slice(1).map(Number))) {
                rule.style.backgroundColor = '#121212';
                log('Changed rule background to #121212:', rule.selectorText);
              }
            } else if (bg && bg.startsWith('#') && bg.length === 7) {
              const [r, g, b] = [1, 3, 5].map(i => parseInt(bg.slice(i, i + 2), 16));
              if (isLightGray(r, g, b)) {
                rule.style.backgroundColor = '#121212';
                log('Changed rule background to #121212:', rule.selectorText);
              }
            }
          });
        } catch (e) { log('Error processing style rule:', e); }
      }
      if (style.innerHTML) {
        style.innerHTML = style.innerHTML.replace(colorRegex, (m, g, r, g2, b, hex) => {
          if (g === '#242526' || g === '#18191a' || g === '#3a3b3c' || g === '#1c1e21' || g === 'rgba(36,37,38,1.0)') return 'background-color:#000000;';
          if (r && g2 && b && isLightGray(+r, +g2, +b)) return 'background-color:#121212;';
          if (hex && isLightGray(...[0, 2, 4].map(i => parseInt(hex.slice(i, i + 2), 16)))) {
            return 'background-color:#121212;';
          }
          return m;
        });
        log('Processed <style> tag innerHTML');
      }
    });

    // Inline styles
    document.querySelectorAll('[style*="background-color"]').forEach(el => {
      const oldStyle = el.getAttribute('style');
      const style = oldStyle.replace(colorRegex, (m, g, r, g2, b, hex) => {
        if (g === '#242526' || g === '#18191a' || g === '#3a3b3c' || g === '#1c1e21' || g === 'rgba(36,37,38,1.0)') return 'background-color:#000000;';
        if (r && g2 && b && isLightGray(+r, +g2, +b)) return 'background-color:#121212;';
        if (hex && isLightGray(...[0, 2, 4].map(i => parseInt(hex.slice(i, i + 2), 16)))) {
          return 'background-color:#121212;';
        }
        return m;
      });
      if (style !== oldStyle) {
        el.setAttribute('style', style);
        log('Changed inline background to AMOLED:', el);
      }
    });

    // Broader: set backgrounds for main containers
    [
      'body',
      'div[role="main"]',
      'div[role="feed"]',
      'div[role="article"]',
      'div[data-pagelet*="FeedUnit"]',
      'div[data-testid="post_container"]',
      'div[data-pagelet*="Stories"]',
      'div[data-pagelet*="RightRail"]',
      'div[data-pagelet*="GroupsFeed"]',
      'div[data-testid="story_container"]',
      'div[data-testid="reel_container"]',
      'div[data-testid="right_column"]',
      'section[role="region"]',
      'div[aria-label*="Sponsored"]',
      'div[aria-label*="Ad"]',
    ].forEach(selector => {
      document.querySelectorAll(selector).forEach(el => {
        if (el && window.getComputedStyle(el).backgroundColor !== 'rgb(0, 0, 0)') {
          el.style.backgroundColor = '#000000';
          log('Set background to #000000 for', selector, el);
        }
      });
    });
  }

  processStyles();

  new MutationObserver(mutations => {
    if (mutations.some(m =>
      (m.type === 'childList' && Array.from(m.addedNodes).some(n =>
        n.tagName === 'STYLE' || (n.nodeType === 1 && n.hasAttribute('style')) ||
        (n.tagName === 'META' && n.getAttribute('name') === 'theme-color'))) ||
      (m.type === 'characterData' && m.target.parentNode?.tagName === 'STYLE') ||
      (m.type === 'attributes' && (m.attributeName === 'style' ||
        (m.target.tagName === 'META' && m.attributeName === 'content'))))
    ) {
      log('MutationObserver triggered, processing styles...');
      processStyles();
    }
  }).observe(document.documentElement, {
    childList: true,
    subtree: true,
    characterData: true,
    attributes: true,
    attributeFilter: ['style', 'content']
  });
})();