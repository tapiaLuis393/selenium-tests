#!/bin/bash
set -e

echo "Descargando ChromeDriver versión fija..."

CHROMEDRIVER_URL="https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/138.0.7204.96/linux64/chromedriver-linux64.zip"

curl -Lo chromedriver.zip "$CHROMEDRIVER_URL"
unzip chromedriver.zip
mv chromedriver-linux64/chromedriver /usr/local/bin/
chmod +x /usr/local/bin/chromedriver

echo "ChromeDriver instalado correctamente."
