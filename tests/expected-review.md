# Expected findings for the fictional demo

These are deliberate examples for testing the translation-quality workflow.

| Key | Expected finding | Why |
|---|---|---|
| `button.cancel` | Warning: terminology | “Stornieren” usually means canceling an order/reservation; “Abbrechen” is the intended generic UI action in this demo glossary. |
| `error.connection` | Error: placeholder mismatch | Source uses `{service}`; target uses `{server}`. The runtime variable name must be preserved exactly. |
| `dialog.unsavedChanges` | Error: missing translation | The source key is absent from the target file. |
| `notification.welcome` | Warning: extra target key | The target includes a key absent from the source file. |

The deterministic script checks missing/extra keys and placeholder mismatches. It will not detect the terminology issue in `button.cancel`; that requires glossary-aware language review.
