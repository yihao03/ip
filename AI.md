# AI.md

<!--toc:start-->
- [AI.md](#aimd)
  - [AI Tool Usage Record](#ai-tool-usage-record)
    - [Overview](#overview)
    - [Tools Used](#tools-used)
    - [Increments and Observations](#increments-and-observations)
    - [Overall Assessment](#overall-assessment)
<!--toc:end-->

## AI Tool Usage Record

### Overview

This document tracks the use of AI tools in the development of the Dumpy JavaFX
chat application. The primary goal is to evaluate AI assistance in software
engineering tasks, focusing on productivity, reliability, and maintaining control
over the codebase.

### Tools Used

- **GitHub Copilot**: Integrated into the development environment (Neovim) through
`zbirenbaum/copilot.lua` and `CopilotC-Nvim/CopilotChat.nvim` for
real-time code suggestions, debugging, and refactoring. Used extensively for
implementing UI components, fixing CSS styling issues, and ensuring code
quality.
  - **ChatGPT 5**: Attempted for repetitive tasks such as refactoring and code
  formatting. Despite being a well-regarded model, it provided generic
  advice that lacked specificity to JavaFX and the project's constraints,
  leading to minimal actionable insights.
  - **Claude Sonnet 4**: Tried for code review and optimization suggestions.
  Although highly regarded, it generated overly verbose or incorrect code
  snippets for JavaFX-specific issues (e.g., FXML loading and CSS application),
  requiring significant manual corrections.
  - Grok Code Fast 1: Most optimal for coding JavaFX, and has been the most
  effective AI model for code generation I have used recently. It was able to
  intelligently identify issues that may lie in other files, and suggesting relevant
  fixes.

### Increments and Observations

- **Increment 1: Conversion from TUI to GUI**
  - Tool: GitHub Copilot
  - Usage: Assisted in generating boilerplate code for Main.java,
  MainWindow.java, and FXML files. Provided quick completions for
  JavaFX scene setup and event handling.
  - What Worked: Saved time on repetitive code (e.g., FXML loaders, button
  actions). Ensured consistent structure.
  - What Didn't: Occasionally suggested outdated JavaFX patterns; required
  verification against documentation.
  - Time Saved: ~20-30% on initial coding.

- **Increment 2: DialogBox Component and Styling**
  - Tool: GitHub Copilot
  - Usage: Helped implement DialogBox.java, including FXML integration, style
  class application, and avatar handling. Suggested fixes for CSS loading
  issues.
  - What Worked: Provided accurate code snippets for dynamic styling and layout
  adjustments. Facilitated quick iterations on bubble designs.
  - What Didn't: Initial suggestions for CSS application to nodes instead of
  scenes led to runtime issues; corrected via manual testing.
  - Time Saved: ~25% on component development.

- **Increment 3: Debugging CSS and Bubble Display**
  - Tool: GitHub Copilot
  - Usage: Diagnosed and fixed the issue where message bubbles weren't
  displaying by moving CSS loading to the scene in Main.java. Added null checks
  for resource loading.
  - What Worked: Identified the root cause (stylesheets attachment) and provided
  targeted code changes.
  - What Didn't: Required multiple iterations to confirm the fix, as AI were
  not able to identify that the error lies outside the context provided.
  - Time Saved: ~15% on debugging.

- **General Observations on ChatGPT 5 and Claude Sonnet 4**
  - Attempts to use these tools for brainstorming project ideas and providing
  multiple approaches to UI design or architecture were not effective. Despite
  their reputation, they often produced high-level, generic responses that
  didn't account for JavaFX specifics or the project's existing codebase. For
  example, suggestions for CSS fixes were incorrect or incomplete, leading to
  wasted time on invalid implementations.
  - However, AI in general excels at brainstorming: it can generate several
  potential approaches quickly, allowing the developer to evaluate and choose
  the best one. This maintains control over the codebase, as the final decision
  and implementation remain with the human developer. For instance, Copilot's
  suggestions for layout fixes provided options (e.g., node vs. scene stylesheets),
  enabling selective adoption.

### Overall Assessment

- AI tools like GitHub Copilot are valuable for productivity in coding tasks,
especially for experienced developers who can critically evaluate suggestions.
They save time on syntax, structure, and common patterns but require manual
oversight for correctness and integration.
- Writing code by hand remains a craft that can be enjoyable at times, fostering
deeper understanding and creativity. AI is best used to speed up redundant or
boring parts, allowing focus on the more engaging aspects of development.
- Total estimated time saved: ~20-25% across the project, primarily in coding
and debugging.
- Recommendation: Use AI for ideation and code generation, but always test and
refine outputs. Avoid relying on standalone AI models for complex,
framework-specific issues without strong prior knowledge.

Last Updated: [15 September 2025]  
Developer: [Hon Yi Hao]
