# 问道山海 · 花瓣飘飞兼容补丁 0.1.0（源码测试版）

目标版本：Minecraft **1.20.1** / Forge **47.4.x** / Java **17**。

**状态：源码已编写，尚未在实际 Forge 客户端编译和运行验证。不要把本源码 ZIP 直接扔进 mods 文件夹。**

## 实现的功能

1. 粒子继承 `net.minecraft.client.particle.CherryParticle`，保留原版樱花花瓣的自旋、曲线飘落方式。
2. 粒子贴图直接引用 Minecraft 1.20.1 原版 `cherry_0` ～ `cherry_11`，**不复制或重新绘制原版贴图**。
3. 配置默认识别 `minecraft:cherry_leaves` 和 `xiaoxiang_cultivation:peach_leaves`，对血桃木尝试通过附近 `blood_peach_log` 区分颜色。
4. 发射位置选在树叶下表面，获取下方地面距离，自动延长存活时间；风向优先使用 Living Wind 1.4.0 的 `ClientWindApi.windXAt/windZAt`。
5. 可选 Mixin 抑制 Rustling Leaves 2.8.3 接管樱花的自然发射，其他树叶飘落、落叶地毯和风力模拟保持原样。
6. 每客户端 tick 限流，所有逻辑仅在客户端运行，不改世界生成，不改掉落物，不改变服务端方块状态。

## 编译

在有网络且装有 Java 17、Gradle 8 的电脑上，打开本源码工程目录执行：

```powershell
gradle build
```

生成的 `build/libs/wendaopetals-forge-1.20.1-0.1.0.jar` 是构建产物，**不能把源码 ZIP 当作 mod**。此环境缺少 ForgeGradle 依赖与 Minecraft 开发版 JAR，无法完成真实编译，因此没有提供未验证的假 JAR。

## 安装后配置

启动并退出一次 Minecraft，打开 `config/wendaopetals-client.toml` 调整：

- `sampleAttempts = 140`：每 tick 检查多少随机区块位置；此值越大，密林花瓣越密，CPU 开销也越高。
- `maxSpawnPerTick = 9`：每 tick 最大新建粒子数量（限流）。
- `horizontalRange = 22`：以玩家为中心的水平取样范围。
- `verticalRange = 18`：以玩家为中心的高度取样范围。
- `maxLifeTicks = 800`：最长飞行40秒；让高处花瓣有机会落到地面。
- `windForce = 0.0035`：水平风推力。
- `flowerLeaves = ["minecraft:cherry_leaves=pink", "xiaoxiang_cultivation:peach_leaves=pink"]`：树叶 ID 与颜色。
- `suppressRustlingCherry = true`：只禁止 Rustling Leaves 对樱花叶原版落花的替代发射，其他普通叶片不关闭。

注意：`setColor` 是 RGB 乘色，原版花瓣 PNG 本身带粉色底。黄/紫/白色的显色精确度不一定够，需要额外准备去色版原版形状的粒子贴图才能做到完全准确。目前先实现默认青丘粉花和小翔桃花、血桃花。

## 实机测试顺序

1. **备份存档，单人新测试世界**。先用原版樱花林验证，确认叶片下方有持续落花。
2. 前往青丘樱花林，原地观察5～10秒，确认不是只有地面花瓣方块。
3. 靠近高处悬崖樱花树，追踪粒子是否跨多个高度层落至地面。
4. 有风和无风时观察偏移，确保风向确实随 Living Wind 变化。
5. 靠近桃树和血桃树，确认数量、颜色；若错误先看注册 ID 与树干检查距离。
6. 打开 Rustling Leaves 的地面落叶与普通树，检查没有重复樱花粒子。
7. 关注 CPU 占用和帧率，必要时把 sampleAttempts 降到 80、maxSpawnPerTick 降到 4。

## 已知限制

- **没做游戏内运行测试，不能宣称与所有 Mod 兼容。**
- 方块采样只识别 `flowerLeaves` 里的条目；没有安装的树木 Mod 不会凭空自动获得花瓣。
- 普通桃树和血桃树共用 `peach_leaves`，附近树干查找是启发式判断，并非绝对准确。
- 原版粒子纹理带粉色，单纯乘色无法生成所有准确的其他颜色。
- 仅客户端使用；多人游戏每位想看效果的玩家分别安装。

## 核心类位置

- `client/PetalClient.java` —— 取样树叶、计算离地高度、限流与生成。
- `client/PetalParticle.java` —— 继承原版 CherryParticle，延长寿命与风力飞行。
- `client/FlowerRule.java` —— 树种颜色、血桃树判别。
- `client/LivingWindCompat.java` —— 可选联动风向。
- `mixin/RustlingCherryMixin.java` —— 取消 Rustling Leaves 对樱花的替代发射。
- `config/PetalConfig.java` —— 用户可调参数。
