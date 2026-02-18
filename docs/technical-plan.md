# Prism Relay — Technical Plan

## Stack
- Kotlin + Android SDK (AppCompat + XML + GridLayout)
- Min SDK 24, Target/Compile 34
- JUnit4 unit tests for beam logic

## Architecture
- `PrismRelayEngine`: pure logic for grid state, mirror rotations, beam tracing
- `MainActivity`: renders 5x5 button grid and status text
- Symbols-only visuals for rapid MVP iteration

## Beam logic
- Start position fixed at source, initial direction RIGHT
- Move cell by cell
- Mirrors reflect direction using deterministic mapping
- End states: goal reached, beam exits grid, or loop detected

## Build/test commands
- `./gradlew test`
- `./gradlew assembleDebug`
