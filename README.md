# ColorCart 🛒

**ColorCart** is an Android app that lets you scan supermarket price tags with OCR and instantly build a live, editable receipt — with color-based subtotals to track spending at a glance.

Instead of waiting for the cashier, you can see how much you're spending **while you shop**, and even break it down by category using simple color highlights.

---

## ✨ Features

* 📸 **Scan price tags with your camera**
* 🔍 **On-device OCR** (no internet required)
* ➕ **Automatic row creation** after each scan (no confirmation popup)
* ✏️ **Inline editing** of item, price, and quantity
* 🎨 **Color highlighting per row**
* 🧮 **Automatic grand total**
* 🟢🟡🔵 **Color-based subtotals**
* 🗑️ **Delete / modify rows anytime**
* ⚡ Fast, minimal, distraction-free UX

---

## 🧠 Concept

ColorCart is not just a receipt app.

It’s a **live shopping calculator** that answers:

* How much am I spending right now?
* How much am I spending on fruits & vegetables?
* How much on baby products or specific categories?

You can **highlight items with colors** (like a marker) and instantly see partial totals.

---

## 📱 Example

| Item     | Price | Qty | Line Total |
| -------- | ----: | --: | ---------: |
| Apples   |  2.40 |   1 |       2.40 |
| Zucchini |  1.80 |   1 |       1.80 |
| Milk     |  1.90 |   1 |       1.90 |
| Yogurt   |  2.20 |   1 |       2.20 |

**Grand Total:** 8.30
**Green Total (Produce):** 4.20
**Yellow Total (Dairy):** 4.10

---

## 🏗️ Tech Stack

* **Kotlin**
* **Jetpack Compose**
* **CameraX** (camera integration)
* **ML Kit Text Recognition** (OCR)
* **Room** (local database)
* **MVVM architecture**

---

## ⚙️ How It Works

1. Tap **"Scan Price Tag"**
2. Take a photo of a supermarket label
3. OCR extracts text (price + name)
4. A new row is added automatically
5. Edit if needed
6. Assign a color to group items
7. View totals instantly

---

## 🚀 Project Status

🚧 Work in progress

---

## 📌 Design Principles

* No friction (no popups during scan)
* Fast interaction
* Offline-first
* User always in control (manual correction)
* Visual understanding via colors

---

## 📄 License

MIT License

---

## ⭐ Why this project?

This project showcases:

* Modern Android development (Compose)
* Computer vision (OCR)
* Real-world UX problem solving
* Clean architecture (MVVM)
* State management & data modeling

---

Feel free to contribute, fork, or use it as inspiration 🚀
