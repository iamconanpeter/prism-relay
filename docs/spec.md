# Prism Relay — Product Spec

## One-liner
A minimalist light-routing puzzle where players rotate mirrors to connect a source beam to a goal crystal.

## Core loop
1. Beam starts from source (S).
2. Player taps mirror cells to rotate slash/backslash orientation.
3. Engine traces beam path in real-time.
4. Puzzle is solved when beam reaches goal (G).

## MVP scope
- Single handcrafted 5x5 puzzle
- Mirror rotation interactions
- Real-time path readout
- Reset button

## Success criteria
- Puzzle understandable in <15 seconds
- Interaction latency feels instant
- Core logic deterministic and test-covered
