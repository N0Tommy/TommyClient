# TommyClient

A Fabric 1.21.5 client-side utility mod for Java 21. This initial release provides a
working foundation: a ClickGUI, toggleable modules, persistent settings, and a
small HUD. It intentionally does **not** include anti-cheat bypasses, stealth,
or public-server combat/attack automation. Any future gameplay automation must
remain limited to local singleplayer or explicitly authorized private testing.

## Included features

- Right Shift opens/closes the ClickGUI.
- Searchable module list and category filter.
- Toggle modules from the GUI.
- Module state and GUI accent color are saved to `config/tommyclient.json`.
- HUD: watermark, FPS, coordinates, and enabled module list.
- Sprint module (local singleplayer only).\n- Perspective toggle (restores the previous camera view when disabled).\n- Armor, keystrokes, and cardinal-direction HUD widgets.\n- Per-module keybind assignment and a quick profile save/load.
- Fullbright module (temporarily sets client gamma to maximum and restores it).
- HUD modules can be toggled independently.

This is a deliberately small, real implementation rather than a list of non-working
placeholder modules. See `FEATURES.md` for implementation status.

## Requirements

- Minecraft **1.21.5**
- Fabric Loader **0.16.14** or newer compatible 1.21.5 release
- Fabric API **0.119.5+1.21.5**
- Java **21**

## Install

1. Install the official Fabric Loader for Minecraft 1.21.5.
2. Start Minecraft once using the Fabric profile, then close it.
3. Download the TommyClient JAR from the project's GitHub Actions artifact (steps below).
4. Put `tommyclient-1.1.0.jar` in your `.minecraft/mods` folder.
5. Ensure Fabric API for 1.21.5 is also in `.minecraft/mods`.
6. Start Minecraft using the Fabric 1.21.5 profile.
7. Press **Right Shift** to open the ClickGUI.

## Build it on GitHub (no Java knowledge needed)

1. Create a new empty repository on GitHub.
2. Upload the contents of this ZIP to the repository (keep the `.github` folder).
3. Open the repository's **Actions** tab.
4. If GitHub asks, enable Actions for the repository.
5. Select **Build TommyClient** and click **Run workflow** (or push a commit).
6. Wait for the green check mark. Open the successful workflow run.
7. Under **Artifacts**, download `tommyclient-build`.
8. Extract the artifact; the compiled mod JAR is inside.

The workflow uses Java 21 and Gradle to resolve Minecraft/Fabric dependencies and run
`gradle build`. If a dependency or mapping version is unavailable, inspect the
workflow log; no successful build is claimed by this ZIP.

## Configure

- Open ClickGUI with Right Shift.
- Click a module row to toggle it.
- Type in the search box to filter modules.
- Use the category buttons to filter modules.
- Settings persist automatically when toggled and on game shutdown.
- Configuration file: `.minecraft/config/tommyclient/default.json`.

## Troubleshooting

- If Minecraft reports a missing dependency, install the matching Fabric API version.
- Confirm the launcher profile is Minecraft 1.21.5 and uses Java 21.
- Remove older duplicate TommyClient JARs from `mods`.
- If the game crashes, temporarily remove TommyClient and attach the latest
  `.minecraft/crash-reports` file when asking for help.

## Development

Run `gradle build` with Gradle 8.12+ and Java 21. The CI workflow is the recommended
build path. The Gradle wrapper scripts are included as convenience launchers, while
the CI workflow provisions Gradle directly.
