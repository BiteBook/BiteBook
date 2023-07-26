const client = filestack.init(FTKEY);

const options = {
    onUploadDone: updateForm,
    maxSize: 10 * 1024 * 1024,
    accept: 'image/*',
    uploadInBackground: false,
};

const picker = client.picker(options);

const btn = document.getElementById('picker');
btn.addEventListener('click', function (e) {
    e.preventDefault();
    picker.open();
});

function updateForm(result) {
    const fileData = result.filesUploaded[0];
    console.log(fileData.url);
    const fileInput = document.getElementById('fileupload');
    fileInput.value = fileData.url;
}
