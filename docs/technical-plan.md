# Prism Relay — Retrofit Technical Plan

## Stack
- Kotlin + Android SDK (GridLayout + AppCompat)
- Min SDK 24 / Target 34
- JUnit4 for deterministic logic tests

## Retrofit Architecture

### 1) Engine (`PrismRelayEngine`)
Added systems:
- puzzle definitions (ID/name/source/goal/par)
- move tracking and solve efficiency score
- star rating from solve quality
- rotation history + limited undo fairness mechanic
- richer `BeamResult` metadata (`stars`, `efficiency`)

### 2) Progress (`RelayProgressManager`)
- daily puzzle index by day-of-year
- persisted best stars per puzzle ID
- daily solve streak tracking

### 3) UI (`MainActivity` + layout)
- dynamic puzzle title with active variant name
- stats row: moves/par/undo/best stars
- undo button with feedback on charge depletion
- color-coded status messaging and streak output on solve
- trace line includes efficiency score

## Differentiation mapping vs low-quality clones
- Clone weakness: one static board → **Fix:** daily variant rotation
- Clone weakness: binary solved/unsolved outcome → **Fix:** stars + efficiency grading
- Clone weakness: punishing accidental tap → **Fix:** bounded undo safety layer

## Test Strategy
- Reflection mapping still verified
- Initial board unsolved state verified
- Solving path gives stars metadata
- Undo returns mirror state and consumes charge

## Validation Commands
- `./gradlew test`
- `./gradlew assembleDebug`
- combined: `./gradlew test assembleDebug`
