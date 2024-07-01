<template>
    <nav class="bg-gray-800 p-4">
        <div class="container mx-auto">
            <div class="flex justify-between items-center">
                <div class="text-yellow-400 font-bold text-xl sm:text-2xl">Craft Exchange</div>
                <div class="hidden md:flex items-center space-x-4">
                    <div v-for="item in headerNavItems" class="text-white hover:text-yellow-400">
                        <NuxtLink :to="item.link">{{ item.key }}</NuxtLink>
                    </div>
                </div>
                <div class="flex items-center space-x-2 sm:space-x-4">
                    <button
                        class="bg-yellow-400 text-black px-2 sm:px-4 py-1 sm:py-2 text-sm sm:text-base rounded hover:bg-yellow-500">
                        Login
                    </button>
                    <button class="md:hidden text-white" @click="toggleMobileMenu()">
                        <Menu />
                    </button>
                </div>
            </div>
            <div v-if="isMobileMenuOpen">
                <div class="md:hidden mt-4 space-y-2">
                    <div v-for="item in headerNavItems" href="#" class="block text-white hover:text-yellow-400">
                        <NuxtLink :to="item.link">{{ item.key }}</NuxtLink>
                    </div>
                </div>
            </div>
        </div>
    </nav>
</template>

<script lang="ts" setup>
import { Menu } from 'lucide-vue-next';
import { ref } from 'vue';

import { headerNavItems } from '@/constants/layout';

const isMobileMenuOpen = ref(false);

const toggleMobileMenu = () => {
    isMobileMenuOpen.value = !isMobileMenuOpen.value;
};
</script>
~/constants/layout