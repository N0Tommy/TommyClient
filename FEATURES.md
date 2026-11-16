# Feature status

This file describes what is present in this source release. A feature is not listed as
implemented unless it has a registered module and code path.

## Implemented

| Feature | Status | Notes |
|---|---|---|
| Module registry | Implemented | Categories, descriptions, enabled state, callbacks, tick hooks |
| ClickGUI | Implemented | Dark three-panel layout, category filter, search, selected-module details |
| Right Shift binding | Implemented | Opens/closes the GUI |
| Per-module keybinds | Implemented | Assign from module detail pane; Backspace clears; persists |
| Persistent configuration | Implemented | JSON in `config/tommyclient/default.json` |
| Quick profile | Implemented | Save/load buttons in selected-module details |
| Sprint | Implemented | Only runs in local singleplayer; stops while a screen is open |
| Fullbright | Implemented | Changes local gamma while enabled and restores previous value |
| Perspective | Implemented | Switches to third-person while enabled and restores previous view |
| Watermark HUD | Implemented | Toggleable |
| FPS HUD | Implemented | Toggleable |
| Coordinates HUD | Implemented | Toggleable |
| Module list HUD | Implemented | Lists enabled non-HUD modules |
| Armor HUD | Implemented | Renders equipped armor icons |
| Keystrokes HUD | Implemented | Displays W/A/S/D/Space state |
| Direction HUD | Implemented | Displays cardinal facing direction |

## Not implemented

Combat automation (aim assist, KillAura, TriggerBot, autoclicker, crystal aura, etc.),
reach/velocity manipulation, anti-knockback, anti-cheat bypasses, packet manipulation,
ESP/X-ray/tracers, freecam, flight/speed/phase, automated mining, scaffold, chest
stealing, auto-farm, structure finding, and advanced inventory automation are not
included in this build. They are not shown as working modules in the GUI.

## Known limitations

- HUD elements currently use fixed positions; an interactive drag-and-drop HUD editor
  is not included.
- The GUI has a single accent color in source rather than a live theme editor.
- Quick profile provides one named profile (`quick`); there is no profile browser or
  import/export UI yet.
- There are no configurable per-module numeric/mode settings in this release.
- Automated gameplay is restricted to local singleplayer in the implemented Sprint
  module. Read-only HUD and visual settings work in any client world.
