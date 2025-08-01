// Hide group suggestions on search page
(() => {
  const hideLastGroupSuggestion = () => {
    if (window.location.href.includes('facebook.com/search')) {
      const parents = document.querySelectorAll('[data-is-h-scrollable]');
      const lastParent = parents[parents.length - 1]?.closest('.m.bg-s1');
      if (lastParent) lastParent.style.display = 'none';
    }
  };

  hideLastGroupSuggestion();
  new MutationObserver(hideLastGroupSuggestion).observe(document.body, { childList: true, subtree: true });
})();

// Hide on comments
(() => {
  const hideGroupContainer = () => {
    if (!window.location.href.includes('facebook.com/story.php?') && !window.location.href.includes('facebook.com/groups')) return;

    const groupContainer = document.querySelector('h3[data-tti-phase="-1"].m');
    if (groupContainer) {
      const parent = groupContainer.closest('.m.bg-s2');
      if (parent) parent.style.display = 'none';
    }
  };

  hideGroupContainer();

  new MutationObserver(hideGroupContainer).observe(document.body, {
    childList: true,
    subtree: true,
  });
})();

// Hide group posts in main feed (desktop)
(() => {
  function hideGroupPosts() {
    // Look for posts/articles in the feed
    const posts = document.querySelectorAll('div[data-pagelet^="FeedUnit"], div[data-testid="post_container"], div[role="article"]');
    posts.forEach(post => {
      // Check for group label or group link
      // 1. Look for a link to /groups/ in the post
      const groupLink = post.querySelector('a[href*="facebook.com/groups/"]');
      // 2. Or look for a label that says "Group" or similar
      const groupLabel = Array.from(post.querySelectorAll('span, div')).find(el => /group/i.test(el.textContent || ''));
      if (groupLink || groupLabel) {
        post.style.display = 'none';
      }
    });
  }
  hideGroupPosts();
  new MutationObserver(hideGroupPosts).observe(document.body, { childList: true, subtree: true });
})();
