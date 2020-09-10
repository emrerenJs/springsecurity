# Spring Security & JWT
## _TR_

Sınıf ve metotların ne iş yaptıkları kendi içerilerinde açıklama satırı olarak
verilmiştir.

### API Route
**HomeController ( / )**

**/ :** DB.json dosyasında tanımlanmış tüm kullanıcılara erişim sağlayabilirsiniz.

**/get-by-username :** URL parametresi (username) kullanarak 
DB.json içine kayıtlı kullanıcıya erişim sağlayabilirsiniz.
(/get-by-username?username=foo)

**/user :**  Kullanıcı sayfasıdır. Sadece giriş yapmış ve
**rol bilgisi USER olan** kişiler erişim sağlayabilir.  





**SecurityController ( /auth )** 

**/login (GET) :** Giriş yapma sayfasıdır. Sadece erişim hatalarında
yönlendirme mesajı için tanımlanmıştır.

**/login (POST) :** Authentication(Giriş) yapma sayfasıdır. Kullanıcı
bilgileri girilmesi doğrultusunda JWT oluşur ve HTTPONLY Cookie olarak ayarlanır.
Token süresi resources/application.properties dosyası içinde saniye cinsinden ayarlanabilir.

## _EN_