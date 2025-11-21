#!/bin/bash
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "                    📊 GraalVM Native Image 构建状态"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# 检查构建进程
if pgrep -f "docker build.*graalvm" > /dev/null; then
    echo "✅ 构建进程: 运行中"
else
    echo "❌ 构建进程: 未运行"
fi

echo ""

# 显示最新日志
if [ -f /tmp/native-build.log ]; then
    echo "📋 最新构建日志 (最后 10 行):"
    echo ""
    tail -10 /tmp/native-build.log | sed 's/^/   /'
    echo ""
    
    # 检查是否完成
    if tail -5 /tmp/native-build.log | grep -q "Successfully tagged\|BUILD SUCCESS"; then
        echo "🎉 构建成功完成！"
        docker images | grep spring-k8s-demo-native
    elif tail -5 /tmp/native-build.log | grep -q "ERROR\|FAILURE"; then
        echo "❌ 构建失败，请查看日志: tail -50 /tmp/native-build.log"
    else
        echo "⏳ 构建进行中..."
    fi
else
    echo "⏳ 等待构建日志..."
fi

echo ""
echo "💡 实时监控: tail -f /tmp/native-build.log"
