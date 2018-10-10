npm run build
git add .
git status
echo "确认提交内容后，输入提交日志："
read gitlog
git commit -m "$gitlog"
git push origin master
git push ezappx master
echo "上传至 ezapp cdn"
scp dist/ezappx-plugin-template.min.js ing@www.ezappx.com:~/cdn/js