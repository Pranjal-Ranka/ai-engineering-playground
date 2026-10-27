# AI Engineering Playground

Small, practical experiments in AI-assisted software engineering. Each experiment should have a clear engineering question, synthetic or public examples, reproducible checks, and documented limitations.

## Experiments

### Translation Quality Codex Skill

A reusable Codex skill for translating and reviewing software localization JSON files.

- Skill: [`.agents/skills/translation-quality/SKILL.md`](.agents/skills/translation-quality/SKILL.md)
- Skill documentation: [`.agents/skills/translation-quality/README.md`](.agents/skills/translation-quality/README.md)
- Fictional example files: [`.agents/skills/translation-quality/examples/`](.agents/skills/translation-quality/examples/)
- Deterministic structure check: [`scripts/check_localization.py`](scripts/check_localization.py)
- Expected findings: [`tests/expected-review.md`](tests/expected-review.md)

Run the example check:

```bash
python scripts/check_localization.py .agents/skills/translation-quality/examples/source.en.json .agents/skills/translation-quality/examples/target.de.json
```

The sample intentionally contains issues, so a non-zero exit code is expected. The helper checks structure and placeholders, not translation meaning.

All localization data in this experiment is fictional.
