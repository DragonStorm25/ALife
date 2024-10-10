# ALife

## Goal

To create a particle simulation that runs on a modified version of physics and chemistry, with some inspiration taken from real life, than can model analogues to real life organic compounds, like phospholipids and amino acids. If analogues can be created and shown to perform similar functions, this would suggest our universes laws of physics and chemistry may not be the only ones that allow life to form.

## Current Features

- Simple charged particles
- Physics engine based on Runge-Kutta method
- Quadtree particle storage to improve simulation speed
- Rendering
- Basic profiler (prints to console, does not work with many particles due to current timing storage and calculation)

## Future Features

- Molecules
  - Formation
  - Partial charges
  - Bonding behavior
- UI
  - Add, move, and delete particles
  - Create, save, and load molecules
  - Show debug info, including profiler
  - Show runtime (in-simulation and real-time)
