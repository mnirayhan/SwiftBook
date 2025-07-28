## MetaPipe - v1.1.0

**First Release: Enhanced Facebook Support**

This release introduces significant improvements to MetaPipe’s Facebook browsing experience, with enhanced ad-blocking capabilities and refined UI optimizations.

### 🚀 Key Enhancements

- Redesigned ad-block script to more reliably remove sponsored posts and ads on Facebook’s **desktop layout**.
- Adopted a robust **text-based detection** strategy for identifying sponsored content, aligned with the mobile logic.
- Expanded selector coverage to include:
  - Main feed posts
  - Stories
  - Reels
  - Sidebar modules
  - Marketplace listings
- Improved **DOM traversal logic** to ensure entire ad containers are accurately detected and removed.
- Retained legacy CSS selectors as a **fallback** mechanism to support older Facebook variants and A/B experiments.
- **Optimized MutationObserver** configuration for improved runtime performance and real-time detection.
- Enabled **media downloads**, including images and videos, directly from the feed.
- Streamlined and **lightweight UI**, focused on speed and minimalism.
- Ongoing maintenance with a **community-driven development model**.

---

> [!IMPORTANT]
> Starting with v1.1.0, MetaPipe will follow a slower release cadence.
> Core userscript functionality will continue to be maintained and updated as needed, without requiring frequent app-level releases.
