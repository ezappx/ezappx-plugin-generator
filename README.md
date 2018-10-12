# EzappxPluginGenerator
ezappx 插件工程生成工具

# 安装
- [下载epg.zip](https://github.com/ingbyr/ezappx-plugin-generator/releases)
- 解压epg.zip，设置环境变量 `path to epg\bin`
- clone [ezappx-plugin-template](https://github.com/ingbyr/ezappx-plugin-templeate) 到工作目录

# 使用方法
```bash
> epg -h
Usage: epg [OPTIONS]

Options:
  -n, --name TEXT               Plugin name
  -t, --template TEXT           Template plugin path. Default path is ezappx-plugin-template
  -ds, --deployScript           Generate deployment script
  -gds, --gitDeployScript       generate git and deployment script
  -epd, --ezappxPluginDir TEXT  Ezappx plugin path
  -h, --help                    Show this message and exit
```

用例：生成名为test-project的工程
```bash
> epg -n test-project
```
或
```bash
> epg
Input the plugin name: test-project
```