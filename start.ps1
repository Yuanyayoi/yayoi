$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
$javaHome = "C:\Program Files\Java\jdk-23_windows-x64_bin\jdk-23.0.1"
$mysqlPath = "C:\Program Files\MySQL\MySQL Server 8.4\bin"

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "    四川国驰恒创科技有限公司官网项目启动脚本" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "[1/6] 设置环境变量..." -ForegroundColor Yellow
$env:JAVA_HOME = $javaHome
$env:PATH = "$mysqlPath;$javaHome\bin;$env:PATH"

Write-Host "[2/6] 检查并释放占用端口..." -ForegroundColor Yellow
$ports = @(8080, 5173, 5174)
foreach ($port in $ports) {
    $process = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue | Where-Object { $_.OwningProcess -ne 0 } | Select-Object -First 1
    if ($process) {
        $processId = $process.OwningProcess
        Write-Host "  释放端口 $port (PID: $processId)..." -ForegroundColor White
        try {
            Stop-Process -Id $processId -Force -ErrorAction SilentlyContinue
        }
        catch {
            Write-Host "  端口 $port 释放失败" -ForegroundColor Red
        }
    }
}
Write-Host "  端口检查完成" -ForegroundColor White

Write-Host "[3/6] 启动MySQL数据库 (端口: 3306)..." -ForegroundColor Yellow
Start-Process -FilePath "mysqld.exe" -WindowStyle Normal -WorkingDirectory "$mysqlPath"

Start-Sleep -Seconds 5

Write-Host "[4/6] 启动后端服务 (端口: 8080)..." -ForegroundColor Yellow
Start-Process -FilePath "cmd.exe" -ArgumentList "/k", "cd /d `"$projectRoot\backend`" && `"$projectRoot\backend\apache-maven-3.9.6\bin\mvn.cmd`" spring-boot:run" -WindowStyle Normal -WorkingDirectory "$projectRoot\backend"

Start-Sleep -Seconds 8

Write-Host "[5/6] 启动公司网站前端 (端口: 5173)..." -ForegroundColor Yellow
Start-Process -FilePath "cmd.exe" -ArgumentList "/k", "cd /d `"$projectRoot\frontend\company-site`" && npm run dev" -WindowStyle Normal -WorkingDirectory "$projectRoot\frontend\company-site"

Write-Host "[6/6] 启动管理后台前端 (端口: 5174)..." -ForegroundColor Yellow
Start-Process -FilePath "cmd.exe" -ArgumentList "/k", "cd /d `"$projectRoot\frontend\admin-panel`" && npm run dev" -WindowStyle Normal -WorkingDirectory "$projectRoot\frontend\admin-panel"

Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Write-Host "    所有服务已启动！" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
Write-Host ""
Write-Host "访问地址：" -ForegroundColor White
Write-Host "  数据库: MySQL @ localhost:3306" -ForegroundColor White
Write-Host "  后端API: http://localhost:8080" -ForegroundColor White
Write-Host "  公司网站: http://localhost:5173" -ForegroundColor White
Write-Host "  管理后台: http://localhost:5174" -ForegroundColor White
Write-Host ""
Write-Host "按任意键退出..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")