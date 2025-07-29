## MetaPipe - v1.2.0

**Facebook Experience & UI Overhaul**

This release brings major improvements to MetaPipe’s Facebook integration, ad-blocking, customization, and user interface.

### 🚀 Key Enhancements

- Redesigned ad-block script for Facebook’s **desktop layout** with improved sponsored post detection and selector coverage.
- "Hide Groups" feature now hides group posts in the main feed, not just suggestions.
- **Feed Customization Dialog** now includes toggles for:
  - Hide suggested posts
  - Hide reels
  - Hide stories
  - Hide people you may know
  - Hide group suggestions
  - Mute keywords (now integrated inside the dialog)
- **Settings Access Redesign:**
  - Removed the settings gear icon from all Facebook navigation bars.
  - **New:** Long-press (5 seconds) the Facebook logo in the navigation bar to open the settings menu.
- **Facebook Lite Mode:**
  - Toggle now works as expected and immediately switches to the Lite UI.
  - "Back to Normal" button returns to the standard Facebook view.
- **UI Consistency:**
  - The long-press settings gesture is now available in all Facebook screens, including the main WebView and Lite mode.
- **Resource Fixes:**
  - Added missing drawable resources for feed customization icons.
  - Fixed missing imports for Compose state collection.

---

> [!IMPORTANT]
> The settings menu is now only accessible by long-pressing the Facebook logo for 5 seconds in the navigation bar, providing a cleaner and more minimal UI.
> All previous bugs related to ad-blocking, group post hiding, and settings access have been addressed in this release.
