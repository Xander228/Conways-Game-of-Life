# Conway's Game of Life

## Overview

Conway's Game of Life is a cellular automaton where each cell can be either "alive" or "dead". The game progresses in discrete steps, with each cell's state being determined by the states of its neighboring cells.

This project provides a graphical user interface (GUI) that allows users to interact with Conway's Game of Life, import/export patterns, and visualize the evolution of the grid.

## Features

- **Grid Simulation**: Visualize Conway's Game of Life with a dynamic grid that updates according to the game's rules.
- **Pattern Import/Export**: Import patterns via file or paste string input, and export patterns to files.
- **File Format Support**: Supports `.rle` and `.txt` files for importing and exporting patterns.
- **User Interface**: Provides a clear, user-friendly interface with options to control the game and manage patterns.

### Running the Game

1.  When the game window opens, you can interact with the grid:
    -   **Set Cells**: Click on the grid to toggle cells between "alive" and "dead".
    -   **Start/Pause**: Start and pause the simulation using the provided buttons.
2.  **Import Patterns**:
    -   Click the "Import" button to open a dialog. You can either:
        -   **Paste an RLE pattern** string into the text area.
        -   **Import from a file**: Use the "Import File" button to choose an `.rle` or `.txt` file from your system.
3.  **Export Patterns**:
    -   After modifying the grid, you can export the current pattern by clicking the "Export File" button, and choosing a location to save the file.

### Pattern Format (RLE)

Patterns can be imported/exported in **RLE (Run-Length Encoding)** format. A basic pattern might look like this:

`#N Glider
#P 1 1
x = 3, y = 3, rule = B3/S23
bo$ obo$ bo!`

-   `b` stands for a "dead" cell.
-   `o` stands for a "live" cell.
-   `$` indicates the end of a row.
-   `!` marks the end of the pattern.

File Import/Export Instructions
-------------------------------

### Importing Patterns:

1.  **Paste a string**: Copy an RLE pattern string and paste it into the text area.
2.  **Import from file**: Click "Import File", choose a `.rle` or `.txt` file containing the pattern, and it will automatically load into the text area.

### Exporting Patterns:

1.  After modifying the grid, click "Export File".
2.  The dialog will ask for a location to save the file, with a default name based on the pattern's name (e.g., `glider.rle`).
