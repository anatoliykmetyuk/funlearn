// Add new card type field functionality
document.addEventListener('DOMContentLoaded', function() {
    const addButton = document.getElementById('add-card-field');
    const cardTypeFields = document.querySelector('.card-type-fields');
    const template = document.querySelector('.card-type-field.template');
    const removeButton = document.getElementById('remove-card-field');

    if (addButton && cardTypeFields && template && removeButton) {
        addButton.addEventListener('click', function() {
            // Get the current number of card type fields (excluding template)
            const currentFields = cardTypeFields.querySelectorAll('.card-type-field:not(.template)');
            const nextIndex = currentFields.length + 1;

            // Clone the template
            const newField = template.cloneNode(true);
            newField.classList.remove('template');
            newField.removeAttribute('hidden');

            // Update IDs and labels to be unique
            const keyInput = newField.querySelector('input[name="keys[]"]');
            const promptInput = newField.querySelector('input[name="prompts[]"]');
            const keyLabel = newField.querySelector('label[for="key_template"]');
            const promptLabel = newField.querySelector('label[for="prompt_template"]');
            const removeButton = newField.querySelector('button[id="remove-card-field"]');

            keyInput.id = `key${nextIndex}`;
            promptInput.id = `prompt${nextIndex}`;
            keyLabel.setAttribute('for', `key${nextIndex}`);
            promptLabel.setAttribute('for', `prompt${nextIndex}`);
            removeButton.id = `remove-card-field${nextIndex}`;
            removeButton.addEventListener('click', function() {
              // Removes the parent card field
              this.parentElement.remove();
            });

            // Add the new field to the container
            cardTypeFields.appendChild(newField);
        });
    }
});
