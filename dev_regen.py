#!/usr/bin/env python3
"""dev_regen.py — مراقب التوليد التلقائي

لمشروع Flask_Compiler: يراقب ملفات الواجهة المصدرية، وبمجرد أن تعدّل عليها
يعيد تشغيل المترجم تلقائياً لإعادة توليد output/.

المصادر المراقَبة:
    - templates/*.jinja        (شكل الواجهة)
    - app.py                   (البيانات/السياق)
    - style.css , script.js    (نسخ مباشرة إلى output/)

يستثني output/ و compiler_output/ حتى لا تتكرر حلقة التوليد.
الاستخدام:  python dev_regen.py
الإيقاف:    Ctrl+C

يتطلب فقط Python 3 (بدون مكتبات خارجية).
"""
import os
import subprocess
import sys
import time
import webbrowser
from pathlib import Path

PROJECT = Path(__file__).resolve().parent
OUT_DIR = PROJECT / "out"
OUTPUT_DIR = PROJECT / "output"
ANTLR_JAR = PROJECT / "src" / "ANTLR" / "antlr-4.13.2-complete.jar"
ARG_FILE = PROJECT / "tmp_argfile.txt"

# الملفات والمسارات التي يُراقب التغيير فيها
WATCHED_FILES = ["app.py", "style.css", "script.js"]
WATCHED_DIRS = ["templates"]
TEMPLATE_EXT = ".jinja"

# مسار classpath لتشغيل المترجم
CP = f"{OUT_DIR};{PROJECT / 'src'};{ANTLR_JAR}"


def source_mtime():
    """أحدث توقيت تعديل بين كل الملفات المصدرية المراقَبة (0 إذا لا شيء)."""
    latest = 0.0
    for name in WATCHED_FILES:
        p = PROJECT / name
        if p.is_file():
            latest = max(latest, p.stat().st_mtime)
    for dirname in WATCHED_DIRS:
        base = PROJECT / dirname
        if base.is_dir():
            for p in base.rglob("*" + TEMPLATE_EXT):
                if p.is_file():
                    latest = max(latest, p.stat().st_mtime)
    return latest


def build():
    """بناء ملفات .class (يُستخدم فقط إن كان out/ غير مبني)."""
    print("[build] تجميع ملفات جافا...")
    subprocess.run(
        ["javac", "-encoding", "UTF-8", "-d", str(OUT_DIR),
         "-cp", str(ANTLR_JAR), "@" + str(ARG_FILE)],
        cwd=str(PROJECT), check=True,
    )
    print("[build] تم البناء.")


def regenerate():
    """إعادة توليد الموقع الثابت في output/ عبر Main ."""
    print("[regen] إعادة توليد output/ ...")
    try:
        subprocess.run(
            ["java", "-cp", CP, "Main", "."],
            cwd=str(PROJECT),
        )
        print("[regen] تم التوليد.")
    except OSError as e:
        print(f"[regen] فشل تشغيل جافا: {e}")


def main():
    print("=" * 58)
    print("  Dev Regen — مراقب إعادة التوليد التلقائي")
    print("  المراقَب: templates/*.jinja, app.py, style.css, script.js")
    print("=" * 58)

    if not (OUT_DIR / "Main.class").exists():
        build()

    print("\n[start] توليد أولي لضمان output/ محدّث...")
    regenerate()

    index_html = OUTPUT_DIR / "index.html"
    if index_html.is_file() and os.environ.get("DEVREGEN_NO_OPEN") != "1":
        webbrowser.open(index_html.as_uri())
        print(f"[start] فتحت المتصفح على {index_html.name} (انعاش المتصفح يدوياً بـ F5)")

    print("\n[watch] الآن أراقب الملفات... عدّل ملف واجهة وسيُولّد تلقائياً (Ctrl+C للإيقاف)\n")

    last = source_mtime()
    try:
        while True:
            time.sleep(0.5)
            current = source_mtime()
            if current <= last:
                continue

            # Debounce: انتظر حتى يهدأ التعديل (لا توليد على كل حفظ متتابع)
            while True:
                time.sleep(0.3)
                quiet = source_mtime()
                if quiet == current:
                    break
                current = quiet

            print("\n[watch] تم رصد تعديل -> إعادة توليد...")
            before = current
            regenerate()

            # لو استمر التعديل أثناء الجولة، نعيد جولة واحدة لضمان النتيجة النهائية
            after = source_mtime()
            if after > before:
                print("[watch] تغيّر الملف أثناء التوليد -> جولة إضافية...")
                regenerate()

            last = source_mtime()
    except KeyboardInterrupt:
        print("\n[stop] تم الإيقاف. وداعاً.")
        sys.exit(0)


if __name__ == "__main__":
    main()