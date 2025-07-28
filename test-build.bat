@echo off
echo ================================
echo   XAI Clean 项目构建测试
echo ================================

echo [测试] 检查项目结构...

if not exist "app\build.gradle" (
    echo [错误] app\build.gradle 文件不存在
    pause
    exit /b 1
)

if not exist "app\src\main\AndroidManifest.xml" (
    echo [错误] AndroidManifest.xml 文件不存在
    pause
    exit /b 1
)

if not exist "app\src\main\java\com\xai\app\MainActivity.kt" (
    echo [错误] MainActivity.kt 文件不存在
    pause
    exit /b 1
)

echo [成功] 所有必要文件都存在！

echo [测试] 检查LOGO文件...
if exist "app\src\main\res\mipmap-hdpi\ic_launcher.png" (
    echo [成功] LOGO文件已就位
) else (
    echo [警告] LOGO文件缺失
)

echo ================================
echo   项目结构检查完成！
echo ================================
echo 
echo 📱 应用特性:
echo - 使用您的原始LOGO
echo - 精美的载入动画（2秒）
echo - 加载 www.xaiswp.app
echo - 100%% 原生Android应用
echo
echo 🚀 GitHub Actions 应该能成功构建此项目
echo

pause