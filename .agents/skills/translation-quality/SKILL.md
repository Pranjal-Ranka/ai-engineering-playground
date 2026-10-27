---
name: translation-quality
description: Translate and review software localization JSON files. Use when asked to translate UI strings, review existing translations, check localization quality, preserve placeholders and markup, or identify strings that need human review.
---

# Translation Quality Skill

Help developers translate and review software localization files without silently changing application behavior.

## Modes

Choose the mode requested by the user:
- **Review**: inspect existing translations and report issues. Do not edit files unless asked.
- **Translate**: translate source strings into the requested target language.
- **Translate + Review**: propose translations, then review them before returning the result.

If the mode, source language, target language, or file scope is unclear and materially affects the result, ask one concise question. Otherwise state reasonable assumptions.

## Safety and file handling

- Work only on the files and languages the user identifies.
- Preserve JSON structure, keys, nesting, data types, and unrelated content.
- Never use company-confidential or customer data in examples or demonstrations.
- Do not claim that a translation is guaranteed correct. Clearly mark uncertainty and recommend native-speaker review for meaning-sensitive strings.
- Do not overwrite source files or apply edits unless the user asks for edits. Prefer a proposed diff when the requested outcome is ambiguous.

## Translation rules

1. Treat source text as the meaning to preserve, not as a template to translate word-for-word.
2. Follow the project's glossary and existing terminology when supplied. If terminology conflicts, flag the conflict rather than silently inventing a rule.
3. Preserve placeholders exactly, including spelling, case, punctuation, and multiplicity. Examples: `{userName}`, `{count}`, `%s`, `%1$d`.
4. Preserve markup and escape sequences such as HTML tags, newline escapes, and JSON escaping. Do not translate identifiers or code.
5. Keep UI wording concise and natural for the target locale. Respect context: a button label, error message, tooltip, and technical explanation may need different wording.
6. Do not infer context that is not provided. Flag ambiguous source strings such as “Open”, “Apply”, or “Save” if context could change the translation.
7. Do not add, remove, or rename localization keys.

## Review workflow

1. Read the source file, target file, and any supplied glossary or style guide.
2. Compare keys and value types. Identify missing and unexpected keys.
3. Compare placeholders and markup between corresponding source and target strings.
4. Review meaning, grammar, tone, terminology consistency, and suitability for the UI context.
5. Report findings with the key, severity, issue, rationale, and a suggested correction when useful.
6. Separate mechanically verifiable problems from language judgments.

Use these severities:
- **error**: structural or technical defect likely to break behavior, such as a missing required key or changed placeholder.
- **warning**: likely mistranslation, inconsistent terminology, grammar issue, or unclear wording.
- **review**: context-dependent wording or a nuance that should be checked by a fluent speaker.

Avoid flagging a string merely because another valid translation is possible. Explain the evidence for each finding and keep the report focused.

## Output format

Start with a short summary. Then list findings in a table with columns:

| Severity | Key | Finding | Suggested action |
|---|---|---|---|

If no issues are found, say that no issues were detected by this review; do not claim the file is error-free. End with a short list of any assumptions or strings that require human judgment.

## Local validation helper

This repository includes `scripts/check_localization.py`, a deterministic helper for JSON key and placeholder checks. If the user asks to run validation and a shell is available, run it on the specified files and report its actual output. The helper does not assess translation quality or prove semantic correctness.
