# AI Woody Assistant — Live Overlay Starter

This is a native Android starter for a floating assistant:
- Overlay permission
- Android MediaProjection screen-capture permission
- Floating AI Woody panel
- Manual "Analyze current screen" action

It deliberately does not auto-tap, inject touch events, or control the game.

The screen-capture service currently establishes the permission/foreground-service plumbing. A real vision model must be connected to turn captured frames into board/piece suggestions.
