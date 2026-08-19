# VIBRANT GOURMET Android Project Rules

You are an Expert Senior Android Developer. Build this **Vibrant Gourmet** food-delivery application as a production-quality, compile-ready, responsive **Jetpack Compose + Material 3** app.

The user-facing brand shown in the supplied references is **Fresh & Friendly**. Keep that visible wordmark unless the user explicitly asks to rename it.

Follow these rules strictly.

---

## 1. SOURCE OF TRUTH

- Always load `@../../design.md` before implementing or modifying UI.
- `design.md` is the primary source for design tokens, typography, spacing, shapes, component intent, and screen structure.
- Do **not** duplicate the complete design specification inside implementation files or these rules.
- The supplied screenshots are the visual reference for:
  - Home
  - Product Detail
  - Your Cart
  - Checkout
  - Track Order / Orders
- For screen-specific hierarchy, proportions, content placement, image crop, active navigation state, and visual emphasis, the supplied screenshot takes precedence over a generic component description.
- The actual files in the supplied asset folder take precedence over abstract image tokens or generic image descriptions in `design.md`.
- Do not redesign the app into a generic restaurant template.
- Do not invent extra screens only to make a button navigate somewhere.

### Preserve reference mock-data inconsistencies

The references are allowed to represent separate demo states.

- The **Your Cart** reference visually shows the **Orders** bottom-navigation item as active. Preserve that composition instead of silently changing the selected item to Home or another destination.
- The **Checkout** order summary contains different food items and totals from the **Your Cart** reference. Do not silently rewrite one screen to match the other when reproducing the supplied references.
- If reference-specific seed data is required, centralize it as named mock scenarios rather than duplicating values inside Composables.

---

## 2. NATIVE ANDROID ARCHITECTURE

This is a **native Android app only**.

- Use Jetpack Compose and Material 3.
- Never use `WebView`, HTML, CSS, JavaScript UI, or a web rendering layer.
- Use:
  - Single Activity architecture
  - MVVM
  - Unidirectional Data Flow
  - immutable UI state
  - `StateFlow`
- Route-level Composables may collect state using `collectAsStateWithLifecycle()`.
- Screen-level and reusable Composables should remain stateless whenever practical.
- Pass state down and events up.
- Reusable UI must not directly own `NavController`, repositories, ViewModels, or mutable application state.
- Keep products, cart state, quantity state, selected payment method, favorite state, promo state, and order-tracking state in ViewModels/repositories or a central local data source.
- Do not introduce a DI framework unless the existing project already uses one.
- Keep package declarations compatible with the real project package structure.

---

## 3. TYPE-SAFE NAVIGATION

Use one centralized `NavHost` and modern Compose Navigation type-safe destinations with `@Serializable`.

Do **not** create string route constants.

Required destinations:

- `Home`
- `Search`
- `Orders`
- `Profile`
- `ProductDetail(productId: String)`
- `Cart`
- `Checkout`

### Bottom navigation

Bottom navigation contains exactly:

- Home
- Search
- Orders
- Profile

Behavior:

- Show bottom navigation on Home, Search, Orders, Profile, and Cart when matching the supplied reference.
- Hide bottom navigation on Product Detail and Checkout.
- `Orders` uses the supplied **Track Order** composition as its primary reference state.
- On the Cart reference, keep the Orders item visually selected as shown in the supplied screenshot.
- Top-level destinations should use `launchSingleTop`, state saving, and state restoration where appropriate.
- Screen Composables receive typed callbacks instead of accessing `NavController` directly.

Recommended demo flow:

`Home -> ProductDetail -> Cart -> Checkout -> Orders`

- Add to Cart must update real local cart state before continuing.
- Proceed to Checkout navigates to Checkout.
- Place Order creates/updates local demo order state and navigates to Orders/Track Order.
- Do not add extra visible navigation controls that do not exist in the reference solely to expose a route.

If Search or Profile has no supplied full-screen reference, keep it intentionally minimal and visually consistent instead of inventing a large feature set.

---

## 4. LOCAL IMAGE ASSET CONTRACT - STRICT

Use the user's real local images. Do not substitute downloaded or AI-generated food photography.

The supplied source filenames are:

| Source file | Android-safe resource name | Intended use |
|---|---|---|
| `Classic Artisan Burger.jpg` | `classic_artisan_burger.jpg` | Cart - Classic Artisan Burger |
| `Crispy French Fries.jpg` | `crispy_french_fries.jpg` | Cart - Crispy French Fries |
| `Luigi's Woodfired Pizza.jpg` | `luigis_woodfired_pizza.jpg` | Home - Luigi's Woodfired Pizza |
| `map.jpg` | `map.jpg` | Orders / Track Order map |
| `Sakura Sushi.jpg` | `sakura_sushi.jpg` | Home - Sakura Sushi |
| `The Gourmet Royale.jpg` | `the_gourmet_royale.jpg` | Product Detail hero image |
| `The Smash Grill.jpg` | `the_smash_grill.jpg` | Home - The Smash Grill |

### Import rules

- If these files are outside the Android module, copy them into an appropriate local resource directory such as `app/src/main/res/drawable-nodpi/`.
- Android resources cannot contain spaces or apostrophes. Rename only during import using the exact Android-safe mapping above.
- Do not invent additional photo resource names.
- Do not download replacements.
- Do not reuse an unrelated supplied photo for a missing food item or person.
- Preserve image aspect ratio.
- Never stretch food photography.
- Use `ContentScale.Crop` where the reference clearly crops a photo.
- For `map.jpg`, keep the important route, scooter, and home marker visible; do not crop away tracking information.

### Missing-photo handling

The supplied asset-folder reference does **not** show a dedicated local photo for:

- Fresh Garden Salad
- driver `Marcus T.`

Do not pretend another supplied image is one of these assets.

If the Android workspace truly contains no matching file:

- report the missing visual asset clearly;
- use a neutral placeholder only when necessary to keep the screen compile-ready;
- keep the placeholder visually unobtrusive and do not present it as a real supplied photo.

### Image loading

`design.md` mentions Coil, but these supplied images are static local resources.

- Do not add Coil **solely** for these local drawable assets.
- Prefer `painterResource` for static local resources unless the existing project already uses Coil consistently.
- If Coil is already part of the project, it may remain in use; do not add another image-loading stack unnecessarily.

---

## 5. TYPOGRAPHY AND BRANDING

Follow the typography specification in `design.md`.

The intended typeface is **Inter**.

- First inspect `app/src/main/res/font/`.
- If Inter exists locally, create and use the correct Compose `FontFamily`.
- Do not guess font resource names.
- Do not download fonts automatically.
- If Inter is unavailable, use the existing project/system sans-serif fallback and keep the project compile-ready.

Branding rules:

- The project/spec name is **Vibrant Gourmet**.
- The visible app wordmark in the supplied screenshots is **Fresh & Friendly**.
- Do not replace that text with Vibrant Gourmet in the UI unless explicitly requested.
- Do not invent a graphical logo asset when none is supplied.

---

## 6. REQUIRED SCREEN BEHAVIOR

### Home

Follow the supplied Home screenshot closely.

Required content includes:

- Fresh & Friendly top branding
- menu action
- search field with `What are you craving?`
- Pizza, Sushi, Burgers, Desserts category row
- Popular Near You section
- food cards with rating badges, price, and Add action
- bottom navigation

Required photo mapping:

- Luigi's Woodfired Pizza -> `luigis_woodfired_pizza`
- The Smash Grill -> `the_smash_grill`
- Sakura Sushi -> `sakura_sushi`

Interactions:

- Search updates local UI state.
- Category selection visibly updates selected state and filters/reorders content when appropriate.
- Tapping a product/card opens typed `ProductDetail(productId)`.
- `+ Add` updates local cart state and must not be a no-op.
- Bottom navigation must work.

Do not create network-backed search or fake API calls.

### Product Detail

Use the supplied **The Gourmet Royale** screenshot as the primary reference.

Preserve:

- large hero image using `the_gourmet_royale`
- circular Back action
- circular Favorite action
- curved/rounded white information surface overlapping the hero section
- title `The Gourmet Royale`
- price `$14.99`
- rating `4.8`
- distance `1.2 km`
- delivery time `15-20 min`
- Description section
- Key Ingredients chips
- bottom quantity selector
- bottom `Add to Cart - $14.99` CTA

Reference ingredient labels:

- Angus Beef
- Brioche Bun
- Lettuce
- House Sauce

Interactions:

- Favorite visibly toggles state.
- Quantity controls mutate quantity and calculated amount when appropriate.
- Add to Cart updates the shared local cart state and provides meaningful continuation to the Cart flow.
- Back action must work.

Do not add unrelated product tabs, reviews pages, customization screens, or remote restaurant data unless requested.

### Your Cart

Closely match the supplied Cart reference.

Reference items:

- Classic Artisan Burger - `$12.50`
- Fresh Garden Salad - `$8.00`
- Crispy French Fries - `$4.50`

Reference summary:

- Subtotal `$33.00`
- Delivery Fee `$2.99`
- Taxes `$3.30`
- Total `$39.29`

Preserve:

- `Your Cart`
- `3 Items`
- item cards with thumbnails and quantity steppers
- Add Promo Code row
- Order Summary card
- large Proceed to Checkout CTA
- reference bottom navigation treatment

Interactions:

- quantity +/- buttons update item quantity and derived totals where the active state is data-driven;
- prevent quantity from going below the supported minimum;
- promo row must have visible local demo behavior such as a dialog/input state or Snackbar;
- Proceed to Checkout navigates to Checkout.

Do not silently replace the missing Fresh Garden Salad photo with one of the supplied burger/pizza/sushi assets.

### Checkout

Follow the supplied Checkout reference.

Preserve:

- back action and centered `Checkout` title
- Delivery Address card
- `Change` action
- Home address content
- Payment Method card
- selected card ending in `4242`
- PayPal option
- Google Pay option
- Add new card action
- Order Summary
- fixed bottom total area
- prominent `Place Order` CTA

Reference visible values include:

- address `123 Culinary Lane, Suite 4B`
- `Food District, NY 10001`
- note `Leave at door, don't ring bell.`
- card expiry `12/25`
- Checkout total `$30.69`

Interactions:

- payment-method selection changes real UI state;
- Change address opens a local demo selector/dialog instead of doing nothing;
- Add new card opens a local demo dialog/form state rather than inventing a full payment product;
- Place Order updates local order state and navigates to Orders/Track Order;
- back action works.

Do not force Checkout's reference order summary to match the Cart screenshot when reproducing the supplied reference state.

### Orders / Track Order

Use the supplied Track Order screenshot as the primary visual reference for the Orders destination.

Preserve:

- Fresh & Friendly top branding
- map card using `map.jpg`
- Estimated Delivery `25 - 30 mins`
- progress indicator
- Order Status stepper
- Order Received
- Preparing
- Out for Delivery
- driver area for `Marcus T.`
- rating `4.9 (124)`
- Contact action
- bottom navigation with Orders active

Interactions:

- Contact must provide visible demo behavior. If no real phone/contact data exists, use a clear Snackbar/dialog rather than inventing a phone number.
- Order status/progress comes from local immutable UI state.
- Do not implement live GPS, Google Maps, sockets, or a delivery backend unless explicitly requested.

### Search and Profile

No complete reference is supplied for these screens.

Keep them deliberately small and consistent with the existing visual language.

Do not invent:

- account-management workflows
- payment history
- messaging
- loyalty systems
- remote search services
- restaurant dashboards

unless requested later.

---

## 7. DATA AND STATE RULES

Keep mock content centralized and use stable IDs.

Recommended models may include:

### Product

- `id`
- `title`
- `description`
- `category`
- `imageRes`
- `price`
- `rating`
- `distance`
- `deliveryTime`
- `ingredients`

### CartItem

- `productId`
- `quantity`
- `note`

### PaymentMethod

- `id`
- `type`
- `displayLabel`
- `isSelected`

### OrderTracking

- `orderId`
- `estimatedDelivery`
- `status`
- `driverName`
- `driverRating`
- `reviewCount`

Do not redefine the same product independently across multiple Composables.

If screen references intentionally use different demo orders, keep those as named centralized reference scenarios, for example separate Cart and Checkout preview/demo seeds.

Important visible controls must never contain empty lambdas.

A Snackbar/dialog/local state change is preferable to:

- a no-op
- a fake backend
- an invented destination
- an unfinished screen

---

## 8. RESPONSIVE COMPOSE LAYOUT

Never hardcode the screenshot's full width or height.

Use Compose layout primitives such as:

- `fillMaxWidth`
- `weight`
- `aspectRatio`
- `LazyColumn`
- `LazyRow`
- adaptive `Row` / `Column`
- `Arrangement.spacedBy`

Use correct `WindowInsets` and account for system bars.

Requirements:

- match the compact-phone references closely;
- preserve readable typography and spacing at common font/display scaling;
- no clipped text;
- no horizontal overflow;
- no stretched photography;
- approximately 48.dp minimum touch targets for interactive controls;
- use lazy containers for long lists;
- use a docked/sticky bottom action area only where the reference clearly shows one.

Do not use fixed spacer hacks to force content to the bottom of a specific emulator height.

---

## 9. REUSABLE COMPONENTS

Create reusable stateless components where appropriate, for example:

- `FreshFriendlyTopBar`
- `FoodBottomNavigation`
- `CategoryItem`
- `FoodCard`
- `RatingBadge`
- `QuantityStepper`
- `PriceRow`
- `PaymentMethodOption`
- `OrderSummaryCard`
- `OrderStatusStep`
- `PrimaryActionButton`

Names may be adapted to existing project conventions.

Reusable components must not directly access ViewModels or `NavController`.

Provide representative Compose previews where practical.

Use meaningful `contentDescription` values for important interactive icons/images, and null only for genuinely decorative visuals.

---

## 10. BUILD AND DELIVERY SAFETY

- Never leave TODOs.
- Never leave visible controls with no-op lambdas.
- Never leave unresolved imports.
- Never invent drawable names that do not exist.
- Never truncate classes or Composables.
- Never comment out required behavior just to make the app compile.
- Prefer stable Material 3 APIs.
- Use `@OptIn` only when required.
- Avoid unnecessary third-party libraries.
- If the project uses a Gradle Version Catalog, add dependencies through the catalog instead of hardcoding versions in module Gradle files.
- Preserve existing project conventions unless they conflict with these rules, `design.md`, or the supplied references.

Before finishing:

1. Build the app.
2. Fix compilation errors and unused imports.
3. Verify every mapped local image resource resolves.
4. Verify Home -> Product Detail navigation.
5. Verify Add to Cart updates state.
6. Verify Cart quantity controls and Checkout navigation.
7. Verify Checkout payment selection and Place Order behavior.
8. Verify Orders/Track Order displays the supplied map asset correctly.
9. Verify all four bottom-navigation destinations.
10. Verify Favorite, promo, Change address, Add new card, Contact, and other visible actions do not silently do nothing.
11. Compare Home, Product Detail, Cart, Checkout, and Track Order against the supplied compact-phone references and correct obvious differences in spacing, hierarchy, typography emphasis, image cropping, and bottom-action placement.

The final implementation must be **compile-ready, responsive, reference-driven, and based on the user's actual local assets rather than guessed substitutes.**