#include <linux/module.h>
#include <linux/proc_fs.h>
#include <linux/seq_file.h>
#include <linux/pci.h>
#include <linux/uaccess.h>

#define PROC_FILENAME "my_lspci"
static char filter_vendor[16] = {0}; // Фильтр по вендору (пример аргумента)

// Функция для вывода данных о PCI-устройствах
static int my_lspci_show(struct seq_file *m, void *v) {
	struct pci_dev *pdev = NULL;
	char vendor_id[16] = {0};

	seq_puts(m, "PCI Devices:\n");
	for_each_pci_dev(pdev) {
    	snprintf(vendor_id, sizeof(vendor_id), "%04x", pdev->vendor);

    	// Применение фильтра (если задан)
    	if (strlen(filter_vendor) && strcmp(vendor_id, filter_vendor) != 0) {
        	continue;
    	}

    	seq_printf(m, "Device: %04x:%04x, Vendor: %04x, Class: %02x\n",
               	pdev->vendor, pdev->device, pdev->vendor, pdev->class >> 8);
	}

	return 0;
}

// Открытие файла /proc
static int my_lspci_open(struct inode *inode, struct file *file) {
	return single_open(file, my_lspci_show, NULL);
}

// Запись в файл /proc для настройки фильтров
static ssize_t my_lspci_write(struct file *file, const char __user *buffer,
                          	size_t count, loff_t *ppos) {
	if (count > sizeof(filter_vendor) - 1)
    	return -EINVAL;

	if (copy_from_user(filter_vendor, buffer, count))
    	return -EFAULT;

	filter_vendor[count] = '\0'; // Завершающий символ
	return count;
}

// Описание операций с файлом
static const struct proc_ops my_lspci_fops = {
	.proc_open = my_lspci_open,
	.proc_read = seq_read,
	.proc_lseek = seq_lseek,
	.proc_release = single_release,
	.proc_write = my_lspci_write,
};

// Инициализация модуля
static int __init my_lspci_init(void) {
	proc_create(PROC_FILENAME, 0666, NULL, &my_lspci_fops);
	printk(KERN_INFO "/proc/%s created\n", PROC_FILENAME);
	return 0;
}

// Завершение работы модуля
static void __exit my_lspci_exit(void) {
	remove_proc_entry(PROC_FILENAME, NULL);
	printk(KERN_INFO "/proc/%s removed\n", PROC_FILENAME);
}

module_init(my_lspci_init);
module_exit(my_lspci_exit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("Your Name");
MODULE_DESCRIPTION("Custom lspci procfs module");
