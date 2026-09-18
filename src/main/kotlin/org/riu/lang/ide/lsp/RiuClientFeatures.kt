package org.riu.lang.ide.lsp

import com.redhat.devtools.lsp4ij.client.features.LSPClientFeatures

class RiuClientFeatures : LSPClientFeatures() {
    init {
        semanticTokensFeature = RiuSemanticTokensFeature()
    }
}
