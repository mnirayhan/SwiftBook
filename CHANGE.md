## MetaPipe - v0.1.2

**Fix: AMOLED Black Feature Now Works on Both Mobile and Desktop**

- Fixed AMOLED black mode to reliably apply true black backgrounds across Facebook's mobile and desktop layouts.
- Added robust logging to help debug and verify background changes.
- Broadened color matching and selectors to catch more Facebook dark and light gray backgrounds.
- Explicitly sets backgrounds for main containers (feed, articles, etc.) to #000000.
- Improved MutationObserver to ensure dynamic content is always styled correctly.

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
