<script setup>
defineProps({
  message: String,
  show: Boolean,
})
</script>

<template>
  <v-snackbar
    :model-value="show"
    timeout="3500"
    location="top"
    class="snack-failure"
    content-class="snack-content"
    :elevation="0"
  >
    <div class="snack-inner">
      <div class="snack-icon-wrap snack-icon-wrap--error">
        <v-icon icon="mdi-alert-circle-outline" size="18" />
      </div>
      <span class="snack-message">{{ message }}</span>
    </div>
  </v-snackbar>
</template>

<style scoped>
/* ── Tokens ─────────────────────────────────────────── */
.snack-failure {
  --ink:    #2b2420;
  --border: #e8e1d8;
}

/* Override Vuetify snackbar wrapper */
.snack-failure :deep(.v-snackbar__wrapper) {
  background: #fff !important;
  border: 1px solid #fcd4d4;
  border-left: 4px solid #a3262c;
  border-radius: 10px !important;
  box-shadow:
    0 4px 6px -1px rgba(43, 36, 32, 0.08),
    0 12px 28px -8px rgba(163, 38, 44, 0.15) !important;
  padding: 0 !important;
  min-width: 280px;
  max-width: 420px;
}

.snack-failure :deep(.v-snackbar__content) {
  padding: 0 !important;
}

/* Inner layout */
.snack-inner {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 18px;
}

.snack-icon-wrap {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  flex-shrink: 0;
}
.snack-icon-wrap--error {
  background: #fef2f2;
  color: #a3262c;
}

.snack-message {
  font-size: 0.875rem;
  font-weight: 500;
  color: #2b2420;
  line-height: 1.4;
}
</style>