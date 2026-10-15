# Fluid X-ray (流体透视)

适用于 **Minecraft 1.21.1 / NeoForge 21.1.x** 的**纯客户端**模组。

提供三种模式：

| 模式 | 说明 |
| --- | --- |
| `off` | 关闭。流体与雾的渲染与原版完全一致。 |
| `default` | **默认模式**。只渲染与空气接触的流体面（液体与方块之间的面全部隐藏），并去掉水/岩浆的浓雾与水下屏幕贴图。 |
| `killFog` | 与 `default` 相同的面剔除，并且**禁用一切迷雾**：距离雾、天空雾、水/岩浆雾、细雪雾、以及失明/黑暗等药水效果雾。 |

> `killFog` 只负责"关雾"，`all`（完全不渲染流体）这个模式已移除。

## 使用方法

### 按键

- 名称：**Toggle Fluid X-ray**
- 分类：**Fluid X-ray**
- 默认按键：**G**

按下后在 `off → default → killFog → off` 之间循环，当前模式会显示在物品栏上方。

### 客户端指令

在聊天栏输入（无需管理员 / 作弊权限，指令只在本地执行，不会发送给服务器）：

```
/fluidxray            切换到下一个模式
/fluidxray off
/fluidxray default
/fluidxray killFog    （别名：kill_fog、killfog）
```

切换后会立即刷新已加载区块的渲染，效果即时生效。

模式会被记忆：缓存在 `config/fluidxray-client.txt`，下次启动游戏时自动沿用上一次切换到的模式。

## 实现原理

| 效果 | 做法 |
| --- | --- |
| 只渲染与空气接触的流体面（`default` / `killFog`） | Mixin 注入 `LiquidBlockRenderer.shouldRenderFace(...)`（NeoForge 新增、接收相邻方块 `BlockState` 的重载），当相邻方块不是空气时强制返回 `false`。原版本来就会剔除"同种流体之间的面"，因此剩下的正好是流体的"空气外表面"。 |
| 去水/岩浆浓雾（`default` / `killFog`） | 监听 `ViewportEvent.RenderFog`，把 `WATER` / `LAVA` 类型的雾近/远平面推到远裁剪面之外并取消事件。 |
| 禁用一切迷雾（仅 `killFog`） | 同一个 `RenderFog` 处理器不再判断类型与模式，对所有雾（距离雾、天空雾、药水效果雾等）一律推开平面并取消事件。 |
| 去掉屏幕上的水下蓝色贴图 | 监听 `RenderBlockScreenEffectEvent`，取消 `WATER` 类型的覆盖层。 |
| 切换后立即生效 | 调用 `LevelRenderer.allChanged()`（等同于 F3+A）重建所有区块网格。 |

客户端指令通过 `RegisterClientCommandsEvent` 注册，由 `ClientCommandHandler` 在本地执行，
因此**不需要 OP 权限**，服务器也无法感知。按键通过 `RegisterKeyMappingsEvent` 注册，
在 `ClientTickEvent.Post` 中轮询。

`@EventBusSubscriber` 自 NeoForge 1.21.1 起会自动判断事件总线（`IModBusEvent` → 模组总线，其余 →
游戏总线），因此不再显式指定 `bus`。Mixin 由 NeoForge 内置支持，通过 `neoforge.mods.toml` 的
`[[mixins]]` 声明，**不需要 refmap**。

## 构建

需要 **JDK 21**。

```bash
# Windows
gradlew.bat build

# Linux / macOS
./gradlew build
```

产物位于 `build/libs/fluidxray-neoforge-1.21.1-<version>.jar`，把它放进 `mods/` 文件夹即可。

> 首次构建会下载并反编译 Minecraft 1.21.1，耗时较长。

## 开发运行

```bash
gradlew.bat runClient
```

## 目录结构

```
src/main/java/io/github/ratherzenith/fluidxray/
├── FluidXray.java                     # 模组入口
├── FluidXrayMode.java                 # off / default / killFog 三个模式
├── FluidXrayState.java                # 当前模式（无 MC 依赖，供 Mixin 使用）
├── FluidXrayStorage.java              # 把模式缓存到 config/fluidxray-client.txt
├── FluidXrayClient.java               # 切换 + 强制重建区块 + 提示
├── FluidXrayKeyBindings.java          # 按键注册（mod 事件总线）
├── ClientEvents.java                  # 事件（指令 / 按键轮询 / 雾 / 屏幕覆盖层）
├── command/FluidXrayCommand.java      # /fluidxray 客户端指令
└── mixin/LiquidBlockRendererMixin.java# 流体面剔除核心逻辑
src/main/resources/
├── fluidxray.mixins.json
├── logo.png                           # 封面（neoforge.mods.toml 的 logoFile）
├── assets/fluidxray/lang/en_us.json   # 按键名称与分类
└── pack.mcmeta
src/main/templates/
└── META-INF/neoforge.mods.toml        # 构建时用 gradle.properties 展开
```

## 说明

模式是**全局**的：开启后对世界中所有流体生效，而不仅是玩家当前所处的那一片流体。这是为了性能考虑——
如果改成"只在入水时生效"，每次玩家在水面上下浮动都要重建所有区块网格，会非常卡顿。

`killFog` 只关闭雾，不改变天空/背景的清屏颜色（例如在水下时远处背景仍偏蓝），因为那属于 `ComputeFogColor`
而非雾本身。
