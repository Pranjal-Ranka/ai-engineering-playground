#!/usr/bin/env python3
"""Check JSON localization key parity and common placeholder preservation.

This deterministic helper intentionally does not judge translation meaning or fluency.
"""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

# Named braces, printf-style placeholders, and ICU-style simple named arguments.
PLACEHOLDER_RE = re.compile(
    r"\{[A-Za-z_][A-Za-z0-9_.-]*\}|%(?:\d+\$)?[+#0\- ]*(?:\d+)?(?:\.\d+)?[sdif]"
)


def load_json(path: Path) -> dict[str, str]:
    with path.open(encoding="utf-8") as handle:
        data = json.load(handle)
    if not isinstance(data, dict):
        raise ValueError(f"{path}: expected a top-level JSON object")
    invalid = [key for key, value in data.items() if not isinstance(key, str) or not isinstance(value, str)]
    if invalid:
        raise ValueError(f"{path}: expected every key and value to be a string; invalid entries: {invalid}")
    return data


def placeholders(value: str) -> list[str]:
    return sorted(PLACEHOLDER_RE.findall(value))


def main() -> int:
    if len(sys.argv) != 3:
        print("Usage: python scripts/check_localization.py SOURCE.json TARGET.json")
        return 2

    source_path, target_path = map(Path, sys.argv[1:])
    try:
        source = load_json(source_path)
        target = load_json(target_path)
    except (OSError, json.JSONDecodeError, ValueError) as exc:
        print(f"ERROR: {exc}")
        return 2

    findings: list[tuple[str, str, str]] = []
    for key in sorted(source.keys() - target.keys()):
        findings.append(("ERROR", key, "Missing target translation"))
    for key in sorted(target.keys() - source.keys()):
        findings.append(("WARNING", key, "Target key has no matching source key"))

    for key in sorted(source.keys() & target.keys()):
        source_placeholders = placeholders(source[key])
        target_placeholders = placeholders(target[key])
        if source_placeholders != target_placeholders:
            findings.append((
                "ERROR",
                key,
                f"Placeholder mismatch: source={source_placeholders}, target={target_placeholders}",
            ))

    if not findings:
        print("No key or placeholder issues detected.")
        return 0

    print(f"Found {len(findings)} localization structure issue(s):")
    for severity, key, message in findings:
        print(f"{severity}: {key}: {message}")
    return 1


if __name__ == "__main__":
    raise SystemExit(main())
