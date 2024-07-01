<template>
    <footer class="bg-gray-800 text-white py-8">
        <div class="container mx-auto px-4">
            <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
                <div v-for="groupItem in footerItems">
                    <h3 class="text-lg font-semibold mb-4">{{ groupItem.key }}</h3>
                    <ul class="space-y-2">
                        <li v-for="childItem in groupItem.items">
                            <div class="hover:text-yellow-400">
                                <NuxtLink :to="childItem.link">{{ childItem.key }}</NuxtLink>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="mt-8 border-t border-gray-700 pt-8 text-sm text-center">
                <p>&copy; {{ year }} Craft Exchange. All rights reserved</p>
            </div>
        </div>
    </footer>
</template>

<script lang="ts" setup>
import { ref } from 'vue';

import { footerItems } from '@/constants/layout';

const year = ref(new Date().getFullYear())

</script>
