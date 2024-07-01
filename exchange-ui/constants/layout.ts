type NavItem = {
  key: string;
  link: string;
};

type GroupNavItem = {
  key: string;
  items: NavItem[];
};

export const headerNavItems: NavItem[] = [
  {
    key: "Buy Crypto",
    link: "/",
  },
  {
    key: "Markets",
    link: "/",
  },
  {
    key: "Trade",
    link: "/",
  },
  {
    key: "Earn",
    link: "/",
  },
];

export const footerItems: GroupNavItem[] = [
  {
    key: "About Us",
    items: [
      {
        key: "About",
        link: "/",
      },
      {
        key: "Contact Us",
        link: "/",
      },
      {
        key: "Careers",
        link: "/",
      },
    ],
  },
  {
    key: "Products",
    items: [
      {
        key: "Exchange",
        link: "/",
      },
      {
        key: "Academy",
        link: "/",
      },
      {
        key: "Wallet",
        link: "/",
      },
    ],
  },
  {
    key: "Resources",
    items: [
      {
        key: "Blog",
        link: "/",
      },
      {
        key: "FAQ",
        link: "/",
      },
      {
        key: "Support",
        link: "/",
      },
    ],
  },
];
