# Prism Relay — Retrofit Plan Mode Spec

## Plan Mode Status
- **Mode:** Codex PLAN MODE (retrofit)
- **Objective:** turn a single-puzzle prototype into a replayable daily puzzle loop with fairness and solve-quality motivation.
- **Guardrail:** keep tiny 2D implementation and deterministic pure logic.

## Q&A Discovery (required)

### 1) Core fantasy and 10-second hook
**Q:** What should players feel immediately?  
**A:** “I can bend light and outsmart the grid.” In 10 seconds they tap a mirror and instantly see beam route changes.

### 2) Why users come back (daily/weekly loop)
**Q:** Why revisit tomorrow?  
**A:** Daily puzzle variant rotation + daily streak + best-star chase per puzzle.

### 3) Session length targets
**Q:** Session targets?  
**A:** 30s experimentation, 2m solve attempt, 5m optimization for better stars.

### 4) Skill vs luck balance
**Q:** Is this skill-based?  
**A:** High skill. Puzzle layout is deterministic; success depends on spatial reasoning and move efficiency.

### 5) Fail-state fairness and frustration controls
**Q:** How avoid harsh dead-ends?  
**A:** Add limited **undo charge** to recover from one accidental tap without fully resetting.

### 6) Difficulty ramp and onboarding
**Q:** How does ramp work?  
**A:** Start with classic board; daily index rotates puzzle variant while maintaining same interaction model.

### 7) Distinctive mechanic vs Android clones
**Q:** What differentiates from low-quality mirror clones?  
**A:** Efficiency stars, daily puzzle identity, and fair undo-based recovery layer.

### 8) Art/animation scope feasible for small team
**Q:** Can this be polished without heavy assets?  
**A:** Yes: symbol-based board + color/state feedback + concise stats line.

### 9) Audio/feedback plan
**Q:** Feedback plan with no audio pipeline?  
**A:** Rich color-coded status text and live stats (moves/par/undo/best stars/efficiency).

### 10) Monetization-safe design
**Q:** Future monetization without dark patterns?  
**A:** Optional cosmetic themes and puzzle packs; no timer locks.

### 11) Technical constraints and performance budgets
**Q:** Constraints?  
**A:** Offline-first, low memory, deterministic logic suitable for unit tests, min SDK 24.

## Assumptions
- Assumption: daily puzzle/streak loop improves D1 retention in puzzle audience.
- Assumption: one undo per run reduces rage quit without reducing challenge.

## USP (1 line)
A minimalist beam-routing puzzler with daily variants, efficiency stars, and a fair one-undo safety net.

## 3 Differentiators
1. Par-move + star grading for mastery replay.
2. Daily puzzle variant auto-selection and streak memory.
3. Limited undo safety to recover accidental taps.

## 3 Retention Hooks
1. Daily streak on solved puzzle.
2. Best-star persistence per puzzle ID.
3. Efficiency score shown per route evaluation.

## 3 Quality Bars
1. Immediate visual cause/effect on mirror tap.
2. Readable status and metrics at all times.
3. Fairness tool (undo) clearly surfaced and limited.

## Retrofit Scope (this run)
### In scope
- Multi-puzzle support and daily puzzle selection
- Move/par/efficiency/star solve grading
- Undo fairness mechanic
- UI polish for stats and status color
- Expanded tests for undo and star scoring

### Post-MVP retrofit
- More puzzle bank
- Hint preview mode
- Lightweight particle pulse on solved state
