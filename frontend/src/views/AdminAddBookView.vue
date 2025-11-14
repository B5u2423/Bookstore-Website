<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth-store';
import { createBook } from '@/api/admin-api';

const router = useRouter();
const authStore = useAuthStore();

// Form data based on BookDTO
const bookForm = ref({
  isbn: '',
  title: '',
  authors: [],
  description: '',
  price: 0,
  inStock: 0
});

// Author management
const authorInput = ref('');
const isLoading = ref(false);
const errorMessage = ref('');
const successMessage = ref('');

// Form validation rules
const rules = {
  required: value => !!value || 'Trường này là bắt buộc',
  minPrice: value => value > 0 || 'Giá phải lớn hơn 0',
  minStock: value => value >= 0 || 'Số lượng không được âm',
  isbn: value => {
    if (!value) return 'ISBN là bắt buộc';
    if (value.length < 10) return 'ISBN phải có ít nhất 10 ký tự';
    return true;
  }
};

// Check admin access
if (!authStore.isAdmin) {
  router.push('/admin/login');
}

function addAuthor() {
  if (authorInput.value.trim()) {
    const author = { authorName: authorInput.value.trim() };
    bookForm.value.authors.push(author);
    authorInput.value = '';
  }
}

function removeAuthor(index) {
  bookForm.value.authors.splice(index, 1);
}

async function submitBook() {
  errorMessage.value = '';
  successMessage.value = '';
  isLoading.value = true;

  try {
    // Validate form
    if (!bookForm.value.isbn || !bookForm.value.title || bookForm.value.authors.length === 0) {
      errorMessage.value = 'Vui lòng điền đầy đủ thông tin bắt buộc';
      return;
    }

    // Create book via admin API
    await createBook(bookForm.value);
    
    successMessage.value = 'Thêm sách thành công!';
    
    // Reset form
    resetForm();
    
    // Navigate back to dashboard after 2 seconds
    setTimeout(() => {
      router.push('/admin/dashboard');
    }, 2000);

  } catch (error) {
    console.error('Error creating book:', error);
    errorMessage.value = error.response?.data?.message || 'Có lỗi xảy ra khi thêm sách';
  } finally {
    isLoading.value = false;
  }
}

function resetForm() {
  bookForm.value = {
    isbn: '',
    title: '',
    authors: [],
    description: '',
    price: 0,
    inStock: 0
  };
  authorInput.value = '';
}

function goBack() {
  router.push('/admin/dashboard');
}
</script>

<template>
  <v-app>
    <v-app-bar color="primary" dark>
      <v-btn icon @click="goBack">
        <v-icon>mdi-arrow-left</v-icon>
      </v-btn>
      <v-app-bar-title>
        <v-icon class="mr-2">mdi-book-plus</v-icon>
        Thêm Sách Mới
      </v-app-bar-title>
      
      <v-spacer></v-spacer>
      
      <v-btn icon @click="authStore.logout(); router.push('/admin/login')">
        <v-icon>mdi-logout</v-icon>
      </v-btn>
    </v-app-bar>

    <v-main>
      <v-container class="pa-4">
        <v-row justify="center">
          <v-col cols="12" md="8" lg="6">
            <v-card class="elevation-3">
              <v-card-title class="text-h5 pa-4">
                <v-icon class="mr-2" color="primary">mdi-book-plus</v-icon>
                Thêm Sách Mới Vào Hệ Thống
              </v-card-title>

              <v-divider></v-divider>

              <v-card-text class="pa-4">
                <!-- Success Message -->
                <v-alert
                  v-if="successMessage"
                  type="success"
                  class="mb-4"
                  :text="successMessage"
                ></v-alert>

                <!-- Error Message -->
                <v-alert
                  v-if="errorMessage"
                  type="error"
                  class="mb-4"
                  :text="errorMessage"
                ></v-alert>

                <v-form @submit.prevent="submitBook">
                  <!-- ISBN Field -->
                  <v-text-field
                    v-model="bookForm.isbn"
                    label="ISBN *"
                    placeholder="Nhập mã ISBN của sách"
                    :rules="[rules.required, rules.isbn]"
                    prepend-icon="mdi-barcode"
                    class="mb-3"
                    outlined
                    required
                  ></v-text-field>

                  <!-- Title Field -->
                  <v-text-field
                    v-model="bookForm.title"
                    label="Tiêu đề sách *"
                    placeholder="Nhập tên sách"
                    :rules="[rules.required]"
                    prepend-icon="mdi-book"
                    class="mb-3"
                    outlined
                    required
                  ></v-text-field>

                  <!-- Authors Section -->
                  <div class="mb-4">
                    <v-text-field
                      v-model="authorInput"
                      label="Thêm tác giả"
                      placeholder="Nhập tên tác giả và nhấn Enter hoặc nút thêm"
                      prepend-icon="mdi-account-edit"
                      append-icon="mdi-plus"
                      @click:append="addAuthor"
                      @keyup.enter="addAuthor"
                      outlined
                    ></v-text-field>

                    <!-- Display Authors -->
                    <div v-if="bookForm.authors.length > 0" class="mt-2">
                      <v-chip
                        v-for="(author, index) in bookForm.authors"
                        :key="index"
                        class="mr-2 mb-2"
                        closable
                        @click:close="removeAuthor(index)"
                        color="primary"
                        variant="elevated"
                      >
                        <v-icon start>mdi-account</v-icon>
                        {{ author.authorName }}
                      </v-chip>
                    </div>

                    <v-alert
                      v-if="bookForm.authors.length === 0"
                      type="info"
                      variant="tonal"
                      class="mt-2"
                      text="Vui lòng thêm ít nhất một tác giả cho sách"
                    ></v-alert>
                  </div>

                  <!-- Description Field -->
                  <v-textarea
                    v-model="bookForm.description"
                    label="Mô tả sách"
                    placeholder="Nhập mô tả chi tiết về sách..."
                    prepend-icon="mdi-text"
                    rows="4"
                    class="mb-3"
                    outlined
                  ></v-textarea>

                  <!-- Price Field -->
                  <v-text-field
                    v-model.number="bookForm.price"
                    label="Giá bán (VNĐ) *"
                    placeholder="Nhập giá bán"
                    :rules="[rules.required, rules.minPrice]"
                    prepend-icon="mdi-cash"
                    suffix="VNĐ"
                    type="number"
                    min="0"
                    step="1000"
                    class="mb-3"
                    outlined
                    required
                  ></v-text-field>

                  <!-- Stock Field -->
                  <v-text-field
                    v-model.number="bookForm.inStock"
                    label="Số lượng tồn kho *"
                    placeholder="Nhập số lượng sách có sẵn"
                    :rules="[rules.required, rules.minStock]"
                    prepend-icon="mdi-package-variant"
                    type="number"
                    min="0"
                    class="mb-4"
                    outlined
                    required
                  ></v-text-field>

                  <!-- Action Buttons -->
                  <div class="d-flex justify-space-between">
                    <v-btn
                      color="grey"
                      variant="outlined"
                      @click="goBack"
                      :disabled="isLoading"
                    >
                      <v-icon start>mdi-arrow-left</v-icon>
                      Quay lại
                    </v-btn>

                    <div class="d-flex gap-2">
                      <v-btn
                        color="orange"
                        variant="outlined"
                        @click="resetForm"
                        :disabled="isLoading"
                      >
                        <v-icon start>mdi-refresh</v-icon>
                        Đặt lại
                      </v-btn>

                      <v-btn
                        color="primary"
                        type="submit"
                        :loading="isLoading"
                        :disabled="!bookForm.isbn || !bookForm.title || bookForm.authors.length === 0"
                      >
                        <v-icon start>mdi-content-save</v-icon>
                        Thêm sách
                      </v-btn>
                    </div>
                  </div>
                </v-form>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<style scoped>
.gap-2 > * + * {
  margin-left: 8px;
}
</style>