using AppResponseWebApi.Helper;
using Newtonsoft.Json;

namespace AppResponseWebApi.Responses
{
    [JsonConverter(typeof(JsonPathConverter))]
    public class ComponentDetail
    {
        [JsonProperty("fields.Title.value")]
        public string Title { get; set; }
        [JsonProperty("fields.Summary.value")]
        public string Summary { get; set; }

        [JsonProperty("fields.BannerImage.value.src")]
        public string BannerImage { get; set; }
    }
}
