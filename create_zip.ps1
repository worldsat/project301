$staging = "d:\jamali\MyProject\project301\bundle_temp"
if (Test-Path $staging) { Remove-Item -Recurse -Force $staging }
New-Item -ItemType Directory -Path "$staging\app\src\main\res\drawable" -Force | Out-Null
New-Item -ItemType Directory -Path "$staging\images" -Force | Out-Null

$htmlFiles = @(
    "index.html",
    "home_screen.html",
    "search_screen.html",
    "detail_screen.html",
    "cart_screen.html",
    "checkout_screen.html",
    "track_order_screen.html",
    "profile_screen.html"
)

foreach ($f in $htmlFiles) {
    Copy-Item "d:\jamali\MyProject\project301\$f" "$staging\$f" -Force
}

$images = Get-ChildItem "d:\jamali\MyProject\project301\app\src\main\res\drawable" -Filter *.jpg
foreach ($img in $images) {
    Copy-Item $img.FullName "$staging\app\src\main\res\drawable\$($img.Name)" -Force
    Copy-Item $img.FullName "$staging\images\$($img.Name)" -Force
}

$readmeContent = @"
# Fresh & Friendly - HTML Screens & UI Kit

این پکیج شامل کلیه صفحات HTML طراحی شده برای اپلیکیشن سفارش غذای Fresh & Friendly به همراه کلیه تصاویر باکیفیت و دارایی‌های گرافیکی است.

## لیست صفحات:
1. index.html - صفحه اصلی پیش‌نمایش و کاتالوگ تمام صفحات
2. home_screen.html - صفحه اصلی اپلیکیشن (داشبورد، دسته‌بندی‌ها، آیتم‌های پرطرفدار و جدیدترین‌ها)
3. search_screen.html - صفحه جستجو با فیلترهای پیشرفته و نتایج
4. detail_screen.html - صفحه جزئیات غذا با هیرو بزرگ، مشخصات و افزودن به سبد
5. cart_screen.html - سبد خرید با مدیریت تعداد، کوپن تخفیف و خلاصه پرداخت
6. checkout_screen.html - تسویه حساب با انتخاب آدرس و روش پرداخت
7. track_order_screen.html - رهگیری زنده سفارش با نقشه شبیه‌سازی‌شده و موقعیت پیک
8. profile_screen.html - پروفایل کاربری، اشتراک VIP، تنظیمات و آدرس‌ها

## پوشه تصاویر:
- app/src/main/res/drawable/ (مورد استفاده مستقیم فایل‌های HTML)
- images/ (نسخه مجزا برای استفاده در ابزارهای طراحی)

## راهنمای ایمپورت به فیگما (Figma):
1. در فیگما پلاگین html.to.design را اجرا کنید.
2. هر فایل HTML را بکشید و داخل پلاگین رها کنید (Drag & Drop).
3. تمامی لایه‌ها، کامپوننت‌ها و استایل‌ها به صورت Auto Layout و قابل ویرایش به فایل فیگمای شما افزوده می‌شوند.
"@

[System.IO.File]::WriteAllText("$staging\README.md", $readmeContent, [System.Text.Encoding]::UTF8)

$zipPath1 = "d:\jamali\MyProject\project301\fresh_friendly_html_ui_kit.zip"
$zipPath2 = "d:\jamali\MyProject\project301\project301.zip"

if (Test-Path $zipPath1) { Remove-Item -Force $zipPath1 }
if (Test-Path $zipPath2) { Remove-Item -Force $zipPath2 }

Add-Type -AssemblyName System.IO.Compression.FileSystem
[System.IO.Compression.ZipFile]::CreateFromDirectory($staging, $zipPath1)
Copy-Item $zipPath1 $zipPath2 -Force

Remove-Item -Recurse -Force $staging

Write-Output "SUCCESS: Created $zipPath1 and $zipPath2"
Get-ChildItem -Path "d:\jamali\MyProject\project301" -Filter *.zip | Select-Object Name, Length
