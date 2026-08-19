# DESIGN.md - Vibrant Gourmet (Jetpack Compose Spec)

This document provides technical specifications for building the Vibrant Gourmet food delivery app using Jetpack Compose and Material 3.

## 1. GLOBAL DESIGN TOKENS

### Color Palette (Material 3)
| Token | Hex Code | Usage |
| :--- | :--- | :--- |
| **Primary** | `#D32F2F` | Main brand color, major CTAs, status indicators. |
| **On-Primary** | `#FFFFFF` | Text/Icons on top of Primary. |
| **Secondary (CTA)** | `#FFB300` | Highlight actions, secondary buttons, active nav states. |
| **On-Secondary** | `#000000` | Text/Icons on top of Secondary. |
| **Surface** | `#F9F9F9` | Default background color. |
| **On-Surface** | `#1C1B1F` | Primary text and high-emphasis icons. |
| **Surface Variant** | `#F3F3F3` | Cards, input fields, and subtle containers. |
| **On-Surface Variant** | `#49454F` | Secondary text, placeholder content. |
| **Outline** | `#DADADA` | Borders, dividers, and low-emphasis strokes. |

### Typography (Inter)
| Scale | Size (sp) | Weight | Line Height |
| :--- | :--- | :--- | :--- |
| **Headline Large** | 32sp | Bold (700) | 40sp |
| **Title Large** | 22sp | SemiBold (600) | 28sp |
| **Body Large** | 16sp | Regular (400) | 24sp |
| **Body Medium** | 14sp | Regular (400) | 20sp |
| **Label Small** | 11sp | Medium (500) | 16sp |

### Spacing & Shapes
- **Outer Margin**: `16.dp` (Standard padding for all screen edges)
- **Section Spacing**: `24.dp` (Vertical gap between major UI blocks)
- **Component Roundness**:
    - **Cards**: `24.dp` (High roundness)
    - **Buttons**: `100.dp` (Fully rounded/pill-shaped)
    - **Input Fields**: `16.dp`
- **Elevation**: Use `Shadow-sm` equivalent (`1.dp` to `2.dp`) for cards on surface.

---

## 2. SCREEN REFERENCE GALLERY

1. **Home Screen (`SCREEN_9`)**: Main discovery hub. Features search, category chips, and a vertical list of popular restaurant cards.
2. **Product Detail (`SCREEN_7`)**: Deep dive into a specific dish. Large hero image, ingredients list, and bottom-docked price/add-to-cart bar.
3. **Your Cart (`SCREEN_8`)**: Itemized list of selections with quantity selectors and "Add Promo" section.
4. **Checkout (`SCREEN_6`)**: Final step. Address selection, payment method toggle, and order summary breakdown.
5. **Track Order (`SCREEN_4`)**: Real-time delivery tracking. Features a map interface, progress stepper, and delivery driver profile.

---

## 3. COMPONENT-LEVEL SPECIFICATIONS

### TopAppBar (Small/Medium)
- **Structure**: `CenterAlignedTopAppBar` or `TopAppBar`.
- **Content**: Left `IconButton` (Menu), Title (Brand logo font), Right `Avatar` or `IconButton` (Profile).
- **Background**: `MaterialTheme.colorScheme.surface`.

### BottomNavBar
- **Structure**: `NavigationBar`.
- **Items**: 4 slots (Home, Search, Orders, Profile).
- **Active State**: Pill-shaped container using `Secondary` color.

### Custom Cards (Food/Restaurant)
- **Container**: `ElevatedCard` or `Surface`.
- **Layout**: `Column` with an `Image` at the top (aspect ratio 16:9), followed by a `Row` for title and rating.
- **Button**: Small "Add" button pinned to bottom-right or full-width at the bottom.

### Input Fields
- **Type**: `OutlinedTextField` or `TextField` with `shape = RoundedCornerShape(16.dp)`.
- **Color**: Container `SurfaceVariant`, no border or very subtle `Outline`.

---

## 4. FIXED IMAGE ASSET REGISTRY

- **Map Interface (`IMAGE_2`, `IMAGE_3`)**: Minimalist, light-styled map assets showing delivery routes with scooter and house icons.
- **Category Icons**: Vector icons for Pizza, Sushi, Burgers, Desserts.
- **Food Photography**: High-resolution, appetizing photos of specific menu items (e.g., "The Gourmet Royale" burger).
- **Driver Profile**: Round avatar for delivery personnel (e.g., "Marcus T.").

---

## 5. AI IMPLEMENTATION SPECIAL INSTRUCTIONS

**System Prompt for AI Code Generators:**
"Implement this UI in Jetpack Compose using Material 3. Architecture must strictly follow **MVVM** with **State-driven UI** patterns (`MutableStateFlow`).
- **Theming**: Use the hex codes from the `GLOBAL DESIGN TOKENS` section. Ensure `Primary` (#D32F2F) is used for all main actions and `Secondary` (#FFB300) for active navigation states.
- **Layouts**: Use `Scaffold` for all screens to manage TopBar/BottomBar consistently. Use `LazyColumn` for lists to ensure performance.
- **Consistency**: Maintain a corner radius of `24.dp` for all cards. Use `pill-shaped` buttons for all primary CTAs.
- **Images**: Use Coil for image loading. Map sections should use a `Box` with the provided map image asset as a background and UI overlays for tracking details."
