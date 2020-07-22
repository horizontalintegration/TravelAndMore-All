using AppResponseWebApi.Responses;
using Flurl.Http;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using System.Threading.Tasks;

namespace AppResponseWebApi.Controllers
{
    [Route("api/[controller]")]
    public class TravelController : ControllerBase
    {
        [HttpGet("Events")]
        public EventPageResponse Events()
        {
            var eventUrl = "http://sc920sc.dev.local/sitecore/api/layout/render/jss?item=/sitecore/content/IndiaHikes/TravelAndMore/Home/Categories/Events&sc_apikey={D22AEC81-9C53-4202-A782-F363BF6D51EF}";
            EventPageResponse response = null;
            Task.Run(async () => response = await GetData<EventPageResponse>(eventUrl, isJss: true)).Wait();

            return response;
        }

        public static async Task<T> GetData<T>(string url, bool isJss = false)
        {
            dynamic response = await url
                .GetJsonAsync();
                
            if (isJss)
            {
                response = response.sitecore.route;
            }
            var objString = JsonConvert.SerializeObject(response);
            T poco = JsonConvert.DeserializeObject<T>(objString);
            return poco;
        }
    }
}
