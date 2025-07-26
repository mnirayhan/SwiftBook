## MetaPipe - v0.1.1

**Fix: Improved Ad Blocking for Desktop Mode**

- Enhanced the adblock script to reliably remove sponsored posts and ads in Facebook's desktop layout.
- Switched to a robust text-based detection method for sponsored content, similar to the mobile approach.
- Added comprehensive selectors to target main feed posts, stories, reels, sidebar, and marketplace ads on desktop.
- Improved DOM traversal to ensure entire sponsored post containers are hidden.
- Kept legacy selectors as a fallback for maximum compatibility.
- Optimized MutationObserver for better performance and real-time ad removal.



>[!IMPORTANT]
> Please expect less frequent releases from this update onward.
> Userscripts will be maintained as needed without requiring additional updates.
