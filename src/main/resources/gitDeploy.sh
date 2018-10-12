npm run build
git add .
git status
echo "确认提交内容后，输入提交日志："
read gitlog
git commit -m "$gitlog"
git push origin master
git push ezappx master
echo "部署到本地工程"
cp dist/plugin-name.min.js ezappx-plugin-dir