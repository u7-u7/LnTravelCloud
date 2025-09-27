拉取代码
bash
# 克隆仓库到本地
git clone https://github.com/u7-u7/LnTravelCloud.git

# 进入项目目录
cd ln_travel

# 拉取远程最新代码
git pull origin main  # 若默认分支是master，替换为git pull origin master



上传代码
bash
# 查看文件变更
git status

# 添加所有变更文件
git add .

# 提交变更（替换为实际提交说明）
git commit -m "your commit message"

# 推送到远程仓库
git push origin master #可以推送develop




常见问题
分支不一致：git checkout 分支名 切换到正确分支
合并冲突：解决冲突文件后重新执行 add -> commit -> push
权限问题：确认 Gitee 账号已获得仓库访问权限