(() => {
  // Try to hide Reels on both desktop and mobile
  function hideReels() {
    // Desktop: Look for sections with aria-label="Reels" or similar
    const desktopReels = Array.from(document.querySelectorAll('[aria-label]'))
      .filter(el => /reels/i.test(el.getAttribute('aria-label')));
    desktopReels.forEach(el => {
      el.style.display = 'none';
      console.log('Hid desktop reels:', el);
    });

    // Mobile: Try to find by text content
    const mobileReels = Array.from(document.querySelectorAll('span, div'))
      .filter(el => el.textContent && /reels/i.test(el.textContent));
    mobileReels.forEach(el => {
      let parent = el.closest('[role="region"], [data-pagelet], [data-visualcompletion]');
      if (parent) {
        parent.style.display = 'none';
        console.log('Hid mobile reels:', parent);
      }
    });
  }

  hideReels();

  new MutationObserver(() => hideReels())
    .observe(document.body, { childList: true, subtree: true });
})();
