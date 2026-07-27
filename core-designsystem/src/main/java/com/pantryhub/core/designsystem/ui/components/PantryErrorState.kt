package com.pantryhub.core.designsystem.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme

@Composable
fun PantryErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(PantryHubTheme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.error_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(PantryHubTheme.spacing.sm))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(PantryHubTheme.spacing.lg))
        PantryButton(onClick = onRetry) {
            Text(stringResource(R.string.retry_action))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PantryErrorStatePreview() {
    PantryHubTheme {
        PantryErrorState(
            message = "Unable to connect to the database.",
            onRetry = {}
        )
    }
}
