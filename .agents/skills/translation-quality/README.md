# Translation Quality (Codex Skill)

A small Codex skill for translating and reviewing software localization JSON files, with an emphasis on preserving placeholders and directing human attention to risky strings.

## Install

Copy the `translation-quality` directory into your Codex skills directory:

```text
~/.agents/skills/translation-quality/
  SKILL.md
```

For a project-specific skill, place it under the repository's `.agents/skills/translation-quality/` directory. Restart or refresh Codex if needed so it discovers the skill. Skill discovery behavior can vary by client/version; consult the Codex documentation for your installed version.

## Use

Ask Codex to:
- “Review the German localization file for translation quality.”
- “Translate this English JSON localization file into German and preserve all placeholders.”
- “Translate and review these strings, then list the ones that need a human reviewer.”

Provide the source and target files and any project glossary. The skill should review only by default; ask explicitly if you want files edited.

## Repository demo

The fictional files in `examples/` contain intentional issues for practice. They are not production translations. The Python helper in `scripts/` checks JSON key parity and placeholder preservation; it does not assess meaning, grammar, or naturalness.

From the repository root, run:

```bash
python scripts/check_localization.py examples/source.en.json examples/target.de.json
```

The example is intentionally imperfect, so findings are expected.

## Limitations

- The skill provides instructions to an AI agent; it is not a translation engine by itself.
- AI review is probabilistic and can miss subtle meaning errors or flag acceptable wording.
- The deterministic helper currently checks JSON object keys and common placeholders only.
- Always have a qualified speaker review high-impact, legal, safety-related, or technically sensitive strings.
